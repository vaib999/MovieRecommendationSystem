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
    
/*  var numberOfReco: Int = _
  var ipFile: String = _
  var userName: String = _
  var movieName: String = _
  var rating: Int = _
  
  numberOfReco = readInt()
  ipFile = readLine()
  userName = readLine()
  movieName = readLine()
  rating = readInt()*/
    
      var userName: String = _
      userName = args(0)
  
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

  
  
/*  val pw = new PrintWriter(new FileOutputStream(new File(ipFile),true));
  pw.println(userName+"\t"+movieName+"\t"+rating)
  pw.close*/
  
/*  val userBasedRatings = Source.fromFile(ipFile).getLines.map 
  { line =>
      val fields = line.split("\t")
      (fields(0).toString(), (fields(1).toString(), fields(2).toDouble))
  }*/

  //userBasedRatings.foreach{ i =>println("This is the rating tuple = " + i )}

  //val recommendations = getRecommendations(userBasedRatings,userName,numberOfReco)
  
  recommendations.foreach(println)

  /**
    * Finds similarity between all item pairs by using a similarity measure on the common ratings given by users to the items
    * [(ratingByUserX,ratingByUserY)] <- Common user ratings for itemPair
    * ((item1,item2),similarity)
    */
  def calculateSimilarity(itemPair: (String, String), userRatingPairs: Iterable[((String,String),((String,Double),(String,Double)))]): ((String, String), Double) = {
    var sumXX, sumYY, sumXY, sumX, sumY, sumOfSquares = 0.0
    var n = 0

    for (userRatingPair <- userRatingPairs) {
      sumX += userRatingPair._2._1._2
      sumY += userRatingPair._2._2._2
      sumXX += Math.pow(userRatingPair._2._1._2, 2)
      sumYY += Math.pow(userRatingPair._2._2._2, 2)
      sumXY += userRatingPair._2._1._2 * userRatingPair._2._2._2
      sumOfSquares += Math.pow(userRatingPair._2._1._2
        - userRatingPair._2._2._2, 2);
      n += 1
    }
    var similarity = calculate_cosine(sumXX, sumYY, sumXY)
    return (itemPair, similarity)
  }


  /**
    * This function Calculates the Cosine similarity.
    * Cosine similarity is a measure of similarity between two non zero vectors(Movie item vectors)
    * of an inner product space that measures
    * the cosine of the angle between them. The cosine of 0° is 1, and it is less than 1 for any other angle.
    * More similar items will have a value close to 1 and dissimilar value can be negative too
    * sine cosine funtion oscillates from -1,0,1
    * */
  def calculate_cosine(sumXX: Double, sumYY: Double, sumXY: Double): Double = {
    val numerator = sumXY
    val denominator = math.sqrt(sumXX) * math.sqrt(sumYY)
    if (denominator != 0) {
      return (numerator / denominator)
    }
    return 0.0
  }

   def topNRecommendations(top_movie:(String, (String, Double)),item_similarity: Map[(String, String), Double]):List[(String)] = {
     
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
     
     val movie_sorted = Nreco.sortBy(x=> x._2).reverse
     
     movie_sorted.foreach(x=>println(x))
     
     val Nmovie = movie_sorted.map(x=> x._1).toList
       
      return Nmovie
     
   }
   
  
  def getRecommendations(userBasedRatings: Iterator[(String, (String, Double))],userName: String): Iterable[(String)] = {
    
  val groupOfUsers = userBasedRatings.toArray.groupBy(_._1)
//((u5,(m1,4.0)),(u5,(m2,3.0)),(u5,(m3,4.0)),(u5,(m4,5.0)),(u5,(m5,4.0)))
//((u6,(m1,4.0)),(u6,(m2,3.0)),(u6,(m3,4.0)),(u6,(m4,4.0)),(u6,(m5,2.0)))
//((u1,(m1,4.0)),(u1,(m2,3.0)),(u1,(m3,4.0)),(u1,(m4,4.0)),(u1,(m5,5.0)))
//((u4,(m1,4.0)),(u4,(m2,3.0)),(u4,(m3,3.0)),(u4,(m4,4.0)),(u4,(m5,4.0)))
//((u3,(m1,5.0)),(u3,(m2,5.0)),(u3,(m3,3.0)),(u3,(m4,3.0)),(u3,(m5,2.0)))
//((u2,(m1,3.0)),(u2,(m2,4.0)),(u2,(m3,5.0)),(u2,(m4,2.0)),(u2,(m5,4.0)))
  
  val itemPairsWithCommonUserRatings = groupOfUsers.flatMap(x => x._2.toArray.combinations(2)).map(x => ((x(0)._1, x(1)._1), (x(0)._2, x(1)._2))).groupBy(x=>(x._2._1._1,(x._2._2._1)))
/*
((m3,m5),List(((u5,u5),((m3,4.0),(m5,4.0))), ((u6,u6),((m3,4.0),(m5,2.0))), ((u1,u1),((m3,4.0),(m5,5.0))), ((u4,u4),((m3,3.0),(m5,4.0))), ((u3,u3),((m3,3.0),(m5,2.0))), ((u2,u2),((m3,5.0),(m5,4.0)))))
*/
  
  val itemSimilarities = itemPairsWithCommonUserRatings.map(p => calculateSimilarity(p._1, p._2))
  
//((m3,m5),0.949339354882104)
//((m1,m5),0.928013241822578)
//((m4,m5),0.9333333333333333)
//((m2,m4),0.9135468796041984)
  
  //itemSimilarities.foreach(x=>println(x))
  groupOfUsers(userName).toList.sortBy(x=> x._2._2).reverse.head
  
  
  val recommendations = topNRecommendations(groupOfUsers(userName).toList.sortBy(x=> x._2._2).reverse.head,itemSimilarities)
  
  //recommendations.foreach(x=>x)

    return recommendations;
  }
}
