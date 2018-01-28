 


	echo $(pwd) >> pwd.txt
	cd /home/kartik/workspace-java/MovieRecommendationSystem/src/scala
	#scalac test.scala
	echo $1 >> username.txt
	scala -cp .:mysql-connector-java-5.1.44-bin.jar MyRecommender "$1" > output.log 2> error.log
