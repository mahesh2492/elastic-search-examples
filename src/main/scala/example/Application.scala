package example

import org.elasticsearch.client.RestHighLevelClient

object Application extends App {

  val client: RestHighLevelClient = ElasticClient.client
  val elasticSearchService = new ElasticSearchService(client)

  //Data for indexing
  val book1 = Book(1, "Gromyko", "True enemies", "ru", 2014, "fantastic")
  val book2 = Book(2, "Strugatsky", "The Final Circle of Paradise", "en", 1965, "fantastic")
  val book3 = Book(3, "Marquez", "One Hundred Years of Solitude", "sp", 1967, "magical realist")
  val book4 = Book(4, "Hemingway", "For Whom the Bell Tolls", "en", 1940, "realist")
  val book5 = Book(5, "Oldi", "I will Take It Myself", "ru", 1998, "fantastic")
  val book6 = Book(6, "Robert", "Rich Dad Poor Dad", "en", 2000, "fantastic")
  val book7 = Book(7, "Paul Cohelo", "The Alchemist", "en", 1988, "magical realist")
  val book8 = Book(8, "George R. R. Martin", "A Game of Thrones", "en", 2000, "fantasy")
  val book9 = Book(9, "Robert C. Martin", "Clean Coder", "en", 2005, "software")
  val book10 = Book(10, "Robert C. Martin", "Clean Code", "en", 2000, "software")
  val book11 = Book(11, "Martin", "Clean Code", "en", 2000, "software")

  val library = List(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10)

  //inserting documents to es
  for (book <- library) {
    elasticSearchService.insert(book)
  }

  //searching data
  val data = elasticSearchService.searchByField("id", 3)

  println("================================ SEARCHED DATA ================================ \n" + data)

  /*
    For updating book information for a particular id
    elasticSearchService.update(1, "yearOfPublishing", 2000)
   This will update the yearOfPublishing of Book having id 1
   */

  /*
  For deleting any document of particular id of book.
  elasticSearchService.delete(1)
  This will delete the book having id 1
   */

  ElasticClient.client.close()


}
