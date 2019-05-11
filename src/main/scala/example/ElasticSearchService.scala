package example

import example.ElasticSearchService._
import org.elasticsearch.action.delete.{DeleteRequest, DeleteResponse}
import org.elasticsearch.action.index.{IndexRequest, IndexResponse}
import org.elasticsearch.action.search.{SearchRequest, SearchResponse}
import org.elasticsearch.action.update.{UpdateRequest, UpdateResponse}
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.{XContentFactory, XContentType}
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import play.api.libs.json.{Format, Json}
import org.slf4j.{Logger, LoggerFactory}

object ElasticSearchService {

  val Index = "library"
  val Type = "book"
}

class ElasticSearchService(client: RestHighLevelClient) {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  implicit val format: Format[Book] = Json.format[Book]

  def insert(book: Book): IndexResponse = {
    logger.info(s"Going to index $book to ES")
    val indexRequest = new IndexRequest(Index, Type, book.id.toString)
    val jsonString = Json.stringify(Json.toJson(book))
    indexRequest.source(jsonString, XContentType.JSON)
    client.index(indexRequest)
  }

  def update(id: Int, fieldName: String, value: Any): UpdateResponse = {
    logger.info(s"Going to update the field $fieldName with value $value id $id to ES")
    val updateRequest = new UpdateRequest(Index, Type, id.toString)
    val builder = XContentFactory.jsonBuilder

    builder.startObject()
    builder.field(fieldName, value)
    builder.endObject()

    updateRequest.doc(builder)
    client.update(updateRequest)
  }

  def delete(id: Int): DeleteResponse = {
    logger.info(s"Going to delete the document of id $id from ES")
    val deleteRequest = new DeleteRequest(Index, Type, id.toString)
    client.delete(deleteRequest)
  }

  def searchByField(fieldName: String, value: Any): SearchResponse = {
    logger.info(s"Search Request received for $fieldName with value $value")
    val searchRequest = new SearchRequest(Index)
    val searchSourceBuilder = new SearchSourceBuilder

    searchSourceBuilder.query(QueryBuilders.matchPhraseQuery(fieldName, value))
    searchRequest.source(searchSourceBuilder)

    client.search(searchRequest)
  }

}
