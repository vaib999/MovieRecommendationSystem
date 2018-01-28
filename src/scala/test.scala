import scala.io.Source
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import java.io._
import scala.collection.mutable.ListBuffer
import java.sql.{Connection,DriverManager}
object MyRecommender extends App
{
    val url = "jdbc:mysql://localhost/mysql"
    val driver = "com.mysql.jdbc.Driver"
    val username = "root"
    val password = "k@rtik09"
    var connection:Connection = _
    val listIter : ListBuffer[(String,(String, Double))] = ListBuffer()
    try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        val statement = connection.createStatement
        val rs = statement.executeQuery("SELECT * FROM movie_recommendation.user_movie_rating")
        //userBasedRatings.foreach(x=> statement.executeUpdate("INSERT INTO movie_recommendation.movies_recommended(username,movie_name,rating) values ('"+x._1+"','"+x._2._1+"','"+x._2._2+"')"))
        
        
        while (rs.next) 
        {
            val username = rs.getString("username")
            val movie = rs.getString("movie_name")
            val userBasedRatings = rs.getDouble("rating")
            
            listIter += ((username,(movie,userBasedRatings)))

        }
    } catch {
        case e: Exception => e.printStackTrace
    }
    connection.close
    
      var userName: String = _
      userName = readLine()
  
  val recommendations = getRecommendations(listIter.toIterator,userName)
  
    try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        val statement = connection.createStatement
        val rs = statement.executeUpdate("DELETE FROM movie_recommendation.movie_recommendation_scala WHERE username = '"+userName+"'")
        
        recommendations.foreach(x=> statement.executeUpdate("INSERT INTO movie_recommendation.movie_recommendation_scala(username,movie_name) values ('"+userName+"','"+x+"')"))
        
    } catch {
        case e: Exception => e.printStackTrace
    }

  /**
    * Finds similarity between all item pairs by using a similarity measure on the common ratings given by users to the items
    */
 
  def calculateSimilarity(itemPair: (String, String), userRatingPairs: Iterable[((String,String),((String,Double),(String,Double)))]): ((String, String), Double) = {
    var sumXX, sumYY, sumXY, sumX, sumY = 0.0

    //x+y+x^2+y^2+xy
    for (userRatingPair <- userRatingPairs) 
    {
      sumX += userRatingPair._2._1._2
      sumY += userRatingPair._2._2._2
      sumXX += Math.pow(userRatingPair._2._1._2, 2)
      sumYY += Math.pow(userRatingPair._2._2._2, 2)
      sumXY += userRatingPair._2._1._2 * userRatingPair._2._2._2
    }
    var similarity = calculate_cosine(sumXX, sumYY, sumXY)
    return (itemPair, similarity)
  }


  def calculate_cosine(sumXX: Double, sumYY: Double, sumXY: Double): Double = {
    val numerator = sumXY
    val denominator = math.sqrt(sumXX) * math.sqrt(sumYY)
    if (denominator != 0) 
    {
      return (numerator / denominator)
    }
    return 0.0
  }

   def topNRecommendations(top_movie:(String, (String, Double)),item_similarity: Map[(String, String), Double]):List[(String)] = {
     
     //list in 
     val Nreco : ListBuffer[(String, Double)] = ListBuffer()

     for(score<-item_similarity)
     {
       if(score._1._1 == top_movie._2._1  )
       {
         Nreco += ((score._1._2,score._2))
       }
       else if(score._1._2 == top_movie._2._1)
       {
         Nreco+=((score._1._1,score._2))
       }
      }
     //highest movie rating first and so on
     val movie_sorted = Nreco.sortBy(x=> x._2).reverse
     
     //Pure movie recommendation without similarity value
     val Nmovie = movie_sorted.map(x=> x._1).toList
       
      return Nmovie
     
   }
   
  //tuples of (username,(moviename,rating)),user asking for recommendation, returns list of movies recommended
  def getRecommendations(userBasedRatings: Iterator[(String, (String, Double))],userName: String): Iterable[(String)] = {
    
  val groupOfUsers = userBasedRatings.toArray.groupBy(_._1)
  //group by users
  //((u5,(m1,4.0)),(u5,(m2,3.0)),(u5,(m3,4.0)),(u5,(m4,5.0)),(u5,(m5,4.0)))
  
  val itemPairsWithCommonUserRatings = groupOfUsers.flatMap(x => x._2.toArray.combinations(2)).map(x => ((x(0)._1, x(1)._1), (x(0)._2, x(1)._2))).groupBy(x=>(x._2._1._1,(x._2._2._1)))
  //movie pairs with two vectors, one rating vector for each movie in which rating is given by users
/*     m1 m2
  u1   3  4
  u2   2  4
  u3   4  5
  u4   3  2*/
  
  /*
((m3,m5),List(((u5,u5),((m3,4.0),(m5,4.0))), ((u6,u6),((m3,4.0),(m5,2.0))), ((u1,u1),((m3,4.0),(m5,5.0))), ((u4,u4),((m3,3.0),(m5,4.0))), ((u3,u3),((m3,3.0),(m5,2.0))), ((u2,u2),((m3,5.0),(m5,4.0)))))
*/
  
  val itemSimilarities = itemPairsWithCommonUserRatings.map(p => calculateSimilarity(p._1, p._2))
//((m3,m5),0.949339354882104)
//((m1,m5),0.928013241822578)
//((m4,m5),0.9333333333333333)
//((m2,m4),0.9135468796041984)
  
  //movie that was given highest rating by current user 
  val recommendations = topNRecommendations(groupOfUsers(userName).toList.sortBy(x=> x._2._2).reverse.head,itemSimilarities)
  
    return recommendations;
  }
}
