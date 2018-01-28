#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <mysql.h>  // sudo apt-get install libmysql++-dev && gcc -o procedural $(mysql_config --cflags) procedural.c $(mysql_config --libs)

char const* fileName = "../../input-files/imdblist.txt";
// To connect to mysql server
char *server = "localhost";
char *user = "root";
char *password = "k@rtik09"; 
char *database = "movie_recommendation";
MYSQL *conn;
MYSQL_RES *res;
MYSQL_ROW row;
int MAXQUERYBUFFER = 121211;
char query[128] = {0};

char *string_trim_inplace(char *s) 
{
  	while (isspace((unsigned char) *s)) s++;
  	if (*s) 
	{
    		char *p = s;
    		while (*p) p++;
    		while (isspace((unsigned char) *(--p)));
    		p[1] = '\0';
  	}
  	return s;
}

double randfrom(double min, double max) 
{
    double range = (max - min); 
    double div = RAND_MAX / range;
    return min + (rand() / div);
}

int main(int argc, char* argv[])
{
	char *movie_name, *genere;
	double rating;	

	// establish connection to mysql database
	conn = mysql_init(NULL);
	if (!mysql_real_connect(conn, server,user, password, database, 0, NULL, 0)) 
	{
      		fprintf(stderr, "%s\n", mysql_error(conn));
      		exit(1);
   	}




    	// ################################################
        // ################ task 2 ########################
        // insert movie_names from imdb list text file

    	FILE* file = fopen(fileName, "r"); 
	char line[256];

	if ( file == 0 )
	{
		printf("ERROR ! Cannot open input file.... Exiting");
		//exit(0);
	}

    	while (fgets(line, sizeof(line), file)) 
	{
		// break the string 
		char *token = strtok(line, ",");
		int counter = 0;
		while (token != NULL)
		{
			token = string_trim_inplace(token);
			if ( counter == 0 )
			{
				// id
			}
			if ( counter == 1 )
			{
                                // movie name
                                printf("Movie name %s\n", token);
                                movie_name = string_trim_inplace(token);
			}
			token = strtok(NULL, ",");
			counter = counter + 1;
			rating  = randfrom(2.0, 10.0);
		}

		// check if movie name already in table
		snprintf(query, MAXQUERYBUFFER, "SELECT * from movie_rating where movie_name = '%s'", movie_name);
		if (!(mysql_query(conn, query)))
            	{
			if ( movie_name != "" )
			{
				res = mysql_store_result(conn);
				int no_of_rows = mysql_num_rows(res);
				if ( no_of_rows == 0 )
				{
					printf("Insert sql\n");
					// movie_name not present
					// prepare to insert data
		        	        snprintf(query, MAXQUERYBUFFER , "INSERT INTO movie_rating (movie_name,rating,delete_status,num_users) VALUES ('%s', '%f','N','1')", movie_name, rating);
		
        			        mysql_query(conn, query);
				}
				else
				{
					printf("Update sql");
					// movie already present, update entries
					snprintf(query, MAXQUERYBUFFER, "UPDATE movie_rating set rating = '%f' where movie_name = '%s'", rating,movie_name);
					mysql_query(conn, query);
				}
			}
		}
		
		// check if movie name already in table
                snprintf(query, MAXQUERYBUFFER, "SELECT * from user_movie_rating where movie_name = '%s' and username ='admin'", movie_name);
                if (!(mysql_query(conn, query)))
                {
			if ( movie_name != "" )
			{
                        	res = mysql_store_result(conn);
                        	int no_of_rows = mysql_num_rows(res);
                        	if ( no_of_rows == 0 )
                        	{
                        	        printf("Insert sql\n");
                        	        // movie_name not present
                        	        // prepare to insert data
                        	        snprintf(query, MAXQUERYBUFFER , "INSERT INTO user_movie_rating (username,movie_name,rating) VALUES ('admin','%s', '%f')", movie_name, rating);
		
                	                mysql_query(conn, query);
                	        }
                	        else
                	        {
                	                // movie already present, update entries
                	                //snprintf(query, MAXQUERYBUFFER, "UPDATE movie_rating set rating = '%f' where movie_name = '%s'", rating,movie_name);
                	                //mysql_query(conn, query);
                	        }
                	}
		}

		snprintf(query, MAXQUERYBUFFER , "delete from movie_rating where movie_name is NULL or movie_name = ''");
	        mysql_query(conn, query);

	        snprintf(query, MAXQUERYBUFFER , "delete from user_movie_rating where movie_name is NULL or movie_name = ''");
	        mysql_query(conn, query);

    	}
	// close file handle
    	fclose(file);



	        // ################################################
        // ################ task 1 ########################
        // delete all marked movies

        snprintf(query, MAXQUERYBUFFER, "SELECT * from movie_rating where delete_status = 'Y'");
        mysql_query(conn, query);
        res = mysql_store_result(conn);
        int no_of_rows = mysql_num_rows(res);

        int num_fields = mysql_num_fields(res);
        while ((row = mysql_fetch_row(res)))
        {
                for(int i = 0; i < num_fields; i++)
                {
                        snprintf(query, MAXQUERYBUFFER, "delete from movie_rating where movie_name = '%s'",row[i]);
                        mysql_query(conn, query);

                        snprintf(query, MAXQUERYBUFFER, "delete from user_movie_rating where movie_name = '%s'",row[i]);
                        mysql_query(conn, query);

                        snprintf(query, MAXQUERYBUFFER, "delete from movie_recommendation_scala where movie_name = '%s'",row[i]);
                        mysql_query(conn, query);

                        printf("%s ", row[i] ? row[i] : "NULL");
                }
                printf("\n");
        }


        // ################################################
        // ################ task 3 ########################
        // delete all marked users

	if (mysql_query(conn, "delete from user_login where delete_status = 'yes'"))
        {
                fprintf(stderr, "%s\n", mysql_error(conn));
                exit(1);
        }
}


// References
// 1. https://www.cyberciti.biz/tips/linux-unix-connect-mysql-c-api-program.html
