#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <mysql.h>  // sudo apt-get install libmysql++-dev && gcc -o procedural $(mysql_config --cflags) procedural.c $(mysql_config --libs)

char const* fileName = "../../input-files/deletemovie.txt";
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
		printf("Delete movie %s" , movie_name);
		snprintf(query, MAXQUERYBUFFER , "update  movie_rating set delete_status = 'Y' where movie_name = '%s'", movie_name);
            	mysql_query(conn, query);
	}
}
