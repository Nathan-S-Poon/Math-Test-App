/*
Stores student's first and last name and photo and a unique number
 */
package curtin.edu.mathtestapp.databases;

public class StudentDbSchema
{
    public static class StudentTable
    {
        public static final String NAME = "students";
        public static class Cols
        {
            public static final String FIRST = "first_name";
            public static final String LAST = "last_name";
            public static final String PHOTO = "photo";
            public static final String ID = "id";

        }

    }


}
