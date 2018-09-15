package models;



public class Variables {
    public static class Menulist {
        public int Menu_gid;
        public int Menu_Parent_gid;
        public String Menu_Name;
        public String Menu_Link;
        public int Menu_Display_Order;
        public int Menu_Level;
        public int Entity_gid;

    }

    public static class employee {

    }

    public static class Customer {
        public Customer(String cust_name, String cust_location, Integer cust_gid) {
            this.cust_name = cust_name;
            this.cust_location = cust_location;
            this.cust_gid = cust_gid;
        }

        public String getCust_name() {
            return cust_name;
        }

        public String getCust_location() {
            return cust_location;
        }

        public Integer getCust_gid() {
            return cust_gid;
        }

        public String cust_name, cust_location;
        public Integer cust_gid;

    }

    public static class ScheduleType {
        public String schedule_type_name;
        public int schedule_type_id;
        public String getSchedule_type_name() {
            return schedule_type_name;
        }

        public int getSchedule_type_id() {
            return schedule_type_id;
        }
    }

    public static class FollowupReason {
        public String followup_name;
        public int followup_id;

        public String getFollowup_name() {
            return followup_name;
        }

        public int getFollowup_id() {
            return followup_id;
        }



    }

    public static class Location{
        public double latlong_lat;
        public double latlong_long;
        public int emp_gid;
        public String latlong_date;
        public int latlong_gid;
        public int entity_gid;
    }

    public static class Product{
        public int product_id;
        public String product_name;
        public String product_code;
    }
}