/*
Stores a list of student id's and their phone numbers
 */
package curtin.edu.mathtestapp.databases;

public class StudentAndPhoneDbSchema
{
    public static class StudentAndPhoneTable
    {
        public static final String NAME = "students_and_phones";
        public static class Cols
        {
            public static final String ID = "id";
            public static final String PHONE = "phone_number";

        }
    }
}
