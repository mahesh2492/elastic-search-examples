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

object ElasticSearchService {

  val Index = "library"
  val Type = "book"
}

class ElasticSearchService(client: RestHighLevelClient) {

  implicit val format: Format[Book] = Json.format[Book]

  def insert(book: Book): IndexResponse = {
    val indexRequest = new IndexRequest(Index, Type, book.id.toString)
    val jsonString = Json.stringify(Json.toJson(book))
    indexRequest.source(jsonString, XContentType.JSON)
    client.index(indexRequest)
  }

  def update(id: String, fieldName: String, value: Any): UpdateResponse = {
    val updateRequest = new UpdateRequest(Index, Type, id)
    val builder = XContentFactory.jsonBuilder

    builder.startObject()
    builder.field(fieldName, value)
    builder.endObject()

    updateRequest.doc(builder)
    client.update(updateRequest)
  }

  def delete(id: String): DeleteResponse = {
    val deleteRequest = new DeleteRequest(Index, Type, id)
    client.delete(deleteRequest)
  }

  def searchByField(fieldName: String, value: Any): SearchResponse = {
    val searchRequest = new SearchRequest(Index)
    val searchSourceBuilder = new SearchSourceBuilder

    searchSourceBuilder.query(QueryBuilders.matchPhraseQuery(fieldName, value))
    searchRequest.source(searchSourceBuilder)

    client.search(searchRequest)
  }

}
