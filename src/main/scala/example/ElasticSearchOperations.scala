package example

import org.elasticsearch.action.delete.{DeleteRequest, DeleteResponse}
import org.elasticsearch.action.index.{IndexRequest, IndexResponse}
import org.elasticsearch.action.search.{SearchRequest, SearchResponse}
import org.elasticsearch.action.update.{UpdateRequest, UpdateResponse}
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.{XContentFactory, XContentType}
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.index.query.QueryBuilders
import play.api.libs.json.{Format, Json}

trait ElasticSearchOperations {

  val client: RestHighLevelClient = ElasticClient.client

  implicit val format: Format[Employee] = Json.format[Employee]
  val index = "employee_index"
  val `type` = "employee"

  def insert(employee: Employee): IndexResponse = {
    val indexRequest = new IndexRequest(index, `type`, employee.id)
    val jsonString = Json.stringify(Json.toJson(employee))
    indexRequest.source(jsonString, XContentType.JSON)
    client.index(indexRequest)
  }

  def update(id: String, fieldName: String, value: Any): UpdateResponse = {
    val updateRequest = new UpdateRequest(index, `type`, id)
    val builder = XContentFactory.jsonBuilder
    builder.startObject()
    builder.field(fieldName, value)
    builder.endObject()
    updateRequest.doc(builder)
    client.update(updateRequest)
  }

  def delete(id: String): DeleteResponse = {
    val deleteRequest = new DeleteRequest(index, `type`, id)
    client.delete(deleteRequest)
  }

  def searchByField(fieldName: String, value: Any): SearchResponse = {
    val searchRequest = new SearchRequest(index)
    val searchSourceBuilder = new SearchSourceBuilder
    searchSourceBuilder.query(QueryBuilders.matchPhraseQuery(fieldName, value))
    searchRequest.source(searchSourceBuilder)
    client.search(searchRequest)
  }

}

object Main extends ElasticSearchOperations with App{

  insert(Employee("1","mahesh","chand",25))
  insert(Employee("2","shivangi","gupta",25))
  insert(Employee("3","nancy","jain",23))
  insert(Employee("4","ankit","barthwal",24))


  update("1","lastName", "kandpal")

  ElasticClient.client.close()
}