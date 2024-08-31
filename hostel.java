import java.sql.*;
import java.util.*;
class hostel {
    Scanner sc = new Scanner(System.in);
    int[] ar = new int[150];

    int std_id = 1;

    String registation() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement sp = con.createStatement();
        ResultSet set = sp.executeQuery("select std_id from student_details order by std_id desc limit 1");
        while (set.next()) {
            std_id = set.getInt(1);
            std_id++;
        }
        PreparedStatement st = con.prepareStatement("insert into student_details(std_id,first_name,name,last_name,phone_no,address,percentage,admission_date,fees) values(?,?,?,?,?,?,?,current_date,0)");
        System.out.println("Enter Your surname");
        String fname = sc.next();
        System.out.println("Enter Your name");
        String name = sc.next();
        System.out.println("Enter Your Father name");
        String lname = sc.next();
        System.out.println("Enter Your Phone number");
        String phoneno = sc.next();
        boolean b= false;
        for(int i=0; i<phoneno.length();i++){
            if(!Character.isDigit(phoneno.charAt(i))){
                b=true;
                break;
            }
        }
        while (phoneno.length() != 10 ||  b ) {
            System.out.print("Enter valid Mobile Number : ");
            phoneno = sc.next();
            for(int i=0; i<phoneno.length();i++){
                if(!Character.isDigit(phoneno.charAt(i))){
                    b=true;
                    break;
                }
                else{
                    b=false;
                }
            }
        }
        long phone = Long.parseLong(phoneno);

        System.out.println("Enter Your address");
        sc.nextLine();
        String address = sc.nextLine();

        System.out.println("Enter Your LastCourse percentage");
        double pr = sc.nextDouble();
        if (pr < 60 || pr > 100) {

            return "-----*You Are Not Aligeble For Hostel*-----";

        }
        st.setInt(1, std_id);
        st.setString(2, fname);
        st.setString(3, name);
        st.setString(4, lname);
        st.setLong(5, phone);
        st.setString(6, address);
        st.setDouble(7, pr);
        int rs = st.executeUpdate();
        allotRoom(std_id);
        stdroom();

        con.close();
        if (rs > 0) {
            return "-----*resitation succesfully*-----";
        } else
            return "resitation not succesfull";
    }

    void adminFunction() {
        System.out.println("1....Add Student");
        System.out.println("2....Serch Student Details");
        System.out.println("3....Remove Student");
        System.out.println("4....Add Student fees");
        System.out.println("5....Generate Fees receipt");
        System.out.println("6....Change Student Room");
        System.out.println("7....Store Student Leave Record");
        System.out.println("8....Display Student Leave Record");
        System.out.println("9....Display FullFees not Pay Student Record");
        System.out.println("10....Clear All Database");
        System.out.println("11....EXIT");
    }


    void notPayFees() throws Exception{
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement sp=con.createStatement();
        ResultSet set =sp.executeQuery("select * from student_details where fees < 40000 order by std_id asc");
        System.out.println("...........................................");
        System.out.println("Std_id\t\tPayFess\t\tNotPayFees");
        System.out.println("...........................................");
        while(set.next()) {
            System.out.println("   "+set.getInt(1)+"\t\t"+ set.getInt(5)+"\t\t"+(set.getInt(5)-40000));
        }
        System.out.println("...........................................");

    }

    void StoreLeaveRecord() throws  Exception{
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement sp=con.createStatement();
        PreparedStatement ps=con.prepareStatement("insert into leave values(?,?,current_date,?)");
        ResultSet set = sp.executeQuery("select std_id from student_details");
        System.out.println("Enter Student Id : ");
        int n =sc.nextInt();
        boolean b=true;
        while(set.next()){
            if(set.getInt(1)== n){
                System.out.println("Enter Reason for Leave : ");
                sc.nextLine();
                String Reason=sc.nextLine();
                System.out.println("Enter Return Date (in yyyy-MM-dd format): ");
                String dateStr = sc.next();
                java.sql.Date returnDate;
                returnDate = java.sql.Date.valueOf(dateStr);
                ps.setInt(1,n);
                ps.setString(2,Reason);
                ps.setDate(3, returnDate);
                ps.executeUpdate();
                b=false;
            }
        }
        if(b){
            System.out.println("----*Student id is not found*----");
        }
        else{
            System.out.println("----*Add Successfull*----");
        }

        con.close();
    }

    void DisplayLeaveRecord() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement sp = con.createStatement();
        boolean b =true;
        System.out.println("1.All Record");
        System.out.println("2.Particuler Student Record");
        System.out.println("Enter Your Choice : ");
        int Choice = sc.nextInt();
        while (Choice != 1 && Choice != 2) {
            System.out.println("Enter Valid Choice :  ");
            Choice = sc.nextInt();
        }
        if (Choice == 1) {
            ResultSet Allset = sp.executeQuery("select * from leave");
            System.out.println("std_id\tReason\tLeaveDate\tReturnDate");
            while (Allset.next()) {
                System.out.println(Allset.getInt(1) + "\t" + Allset.getString(2) + "\t" + Allset.getString(3) + "\t" + Allset.getString(4));
                b=false;
            }
        }
        if (Choice == 2) {
            System.out.println("Enter Student Id : ");
            int n = sc.nextInt();
            ResultSet set = sp.executeQuery("select * from leave where std_id=" + n);
            System.out.println("std_id\tReason\tLeaveDate\tReturnDate");
            while (set.next()) {
                System.out.println(set.getInt(1) + "\t" + set.getString(2) + "\t" + set.getString(3) + "\t" + set.getString(4));
                b=false;
            }
        }
        if(b){
            System.out.println("-----*Record Is Empty*----");
        }
        con.close();
    }

    void feesReceipt () throws Exception{
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement sp = con.createStatement();
        ResultSet set = sp.executeQuery("select * from student_details inner join room on student_details.std_id=room.std_id");
        System.out.println("Enter Student Id");
        int n = sc.nextInt();
        boolean b = true;
        while (set.next()) {
            int tstd = set.getInt(1);
            int fees =set.getInt(5);
            if (tstd == n && fees != 0) {
                System.out.println("................................................");
                System.out.println("           HK's Hostel Fees Receipt                      ");
                System.out.println("................................................");
                System.out.println("student id is : " + tstd+"\nStudent Room No is : "+set.getInt(11)+"\nStudent Bad no is : "+set.getString(12) + "\nstudent name : " + set.getString(2) + set.getString(3) + set.getString(4) + "\nstudent phone no : " + set.getLong(7)
                        + "\nstudent address is : " + set.getString(8)+"\nTotal Pay fees is : "+set.getInt(5) );
                b = false;
                break;
            }
        }
        if (b) {
            System.out.println("-----*Student Is Not Found OR Student not Pay Fees*-----");
        }
        else {
            System.out.println("-----*Fees Receipt generated successfully*-----");
        }
        con.close();
    }

    void changeRoom() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("select * from room order by room_no asc");
        System.out.println("---------------------------------");
        System.out.println("Student id    Room no     Bad no");
        System.out.println("---------------------------------");
        while (set.next()) {
            System.out.println(set.getInt(1) + "            " + set.getInt(2) + "           " + set.getString(3));
        }
        System.out.println("---------------------------------");
        set = st.executeQuery("select * from student_details");
        System.out.print("Enter Student id : ");
        int n = sc.nextInt();
        boolean b = true;
        while (set.next()) {
            int tstd = set.getInt(1);
            if (tstd == n) {
                System.out.println("Enter Room no : ");
                int room = sc.nextInt();
                System.out.println("Enter Bad no : ");
                String s = sc.next();
                PreparedStatement selectStatement = con.prepareStatement("SELECT std_id FROM room WHERE room_no = ? AND bad_no = ?");
                selectStatement.setInt(1, room);
                selectStatement.setString(2, s);
                ResultSet dset = selectStatement.executeQuery();
                st.executeUpdate("delete from room where std_id=" + n);
                st.executeUpdate("Update room set std_id=" + n + " where room_no=" + room + " and bad_no ='" + s + "'");
                dset.next();
                int tempstd = dset.getInt(1);
                if (tempstd != 0) {
                    allotRoom(tempstd);
                }
                b = false;
                break;
            }
        }
        if (b) {
            System.out.println("-----*Student Is Not Found*-----");
        } else {
            System.out.println("-----*Room Change successfully*-----");
        }
        con.close();
    }
    void stdroom() throws Exception{
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement st = con.createStatement();
        ResultSet set= st.executeQuery("select * from room where std_id="+std_id);
        set.next();
        System.out.println("Your Student id is : " + std_id);
        System.out.println("Your Alloted Room no is : "+set.getInt(2)+" And Bad no is : "+set.getString(3));
        con.close();
    }

    void allotRoom(int std_id) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement st = con.createStatement();
        for (int i = 0; i < ar.length; i++) {
            ar[i] = i + 1;
        }
        for (int i = 0; i < ar.length; i++) {
            ResultSet set = st.executeQuery("select * from room order by std_id Asc");
            int n = ar[i];
            int dummy = 0;
            while (set.next()) {
                dummy = set.getInt(2);
                if (dummy == n) {
                    break;
                }
            }
            if (dummy == n) {
                ResultSet dset = st.executeQuery("select bad_no from room where room_no=" + n + " order by bad_no asc");
                if (dset.next()) {
                    if (dset.getString(1).equals("A")) {
                        if (dset.next()) {
                            if (dset.getString(1).equals("B")) {
                                if (dset.next()) {
                                    if (dset.getString(1).equals("C")) {
                                        if (dset.next()) {
                                            if (dset.getString(1).equals("D")) {

                                            } else {
                                                st.executeUpdate("insert into room values(" + std_id + "," + n + ",'D')");
                                                break;
                                            }
                                        } else {
                                            st.executeUpdate("insert into room values(" + std_id + "," + n + ",'D')");
                                            break;
                                        }
                                    } else {
                                        st.executeUpdate("insert into room values(" + std_id + "," + n + ",'C')");
                                        break;
                                    }
                                } else {
                                    st.executeUpdate("insert into room values(" + std_id + "," + n + ",'C')");
                                    break;
                                }
                            } else {
                                st.executeUpdate("insert into room values(" + std_id + "," + n + ",'B')");
                                break;
                            }
                        } else {
                            st.executeUpdate("insert into room values(" + std_id + "," + n + ",'B')");
                            break;
                        }
                    } else {
                        st.executeUpdate("insert into room values(" + std_id + "," + n + ",'A')");
                        break;
                    }
                } else {
                    st.executeUpdate("insert into room values(" + std_id + "," + n + ",'A')");
                    break;
                }
            } else {
                st.executeUpdate("insert into room values(" + std_id + "," + n + ",'A')");
                break;
            }
        }
        con.close();
    }

    void serchStudent() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement sp = con.createStatement();
        ResultSet set = sp.executeQuery("select * from student_details inner join room on student_details.std_id=room.std_id");
        System.out.println("Enter Student Id");
        int n = sc.nextInt();
        boolean b = true;
        while (set.next()) {
            int tstd = set.getInt(1);
            if (tstd == n) {
                System.out.println("student id is : " + tstd + "\nstudent name : " + set.getString(2) + set.getString(3) + set.getString(4) + "\nstudent phone no : " + set.getLong(7)
                        + "\nstudent address is : " + set.getString(8) + "\nstudent percentage is : " + set.getDouble(9)+"%"+"\nStudent Room No is : "+set.getInt(11)+"\nStudent Bad no is : "+set.getString(12));
                b = false;
                break;
            }
        }
        if (b) {
            System.out.println("-----*Student Is Not Found*-----");
        }
        con.close();
    }

    void addFees() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement sp = con.createStatement();
        ResultSet set = sp.executeQuery("select * from student_details");
        System.out.println("Enter Student Id");
        int n = sc.nextInt();
        boolean b = true;
        boolean overamount_fees= false;
        while (set.next()) {
            int tstd = set.getInt(1);
            if (tstd == n) {
                System.out.println("Enter Fees :");
                int previouseFees=set.getInt(5);
                int fees = sc.nextInt()+previouseFees;
                if(fees>40000){
                    System.out.println("----*Fees is OverAmount*----");
                    overamount_fees=true;
                    b=false;
                }else {
                    sp.executeUpdate("Update student_details set fees=" + fees + " where std_id =" + n);
                    b = false;
                    break;
                }
            }
        }
        if (b) {
            System.out.println("-----*Student Is Not Found *-----");
        }
        else if(overamount_fees){
        }
        else {
            System.out.println("-----*Fees Add Successfully*-----");
        }
        con.close();
    }

    void removestd() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement sp = con.createStatement();
        ResultSet set = sp.executeQuery("select * from student_details");
        System.out.println("Enter Student Id");
        int n = sc.nextInt();
        boolean b = true;
        while (set.next()) {
            int tstd = set.getInt(1);
            if (tstd == n) {
                sp.executeUpdate("delete from student_details where std_id=" + n);
                sp.executeUpdate("delete from room where std_id=" + n);
                b = false;
                break;
            }
        }
        if (b) {
            System.out.println("-----*Student Is Not Found*-----");
        } else {
            System.out.println("-----*Student Remove Sucessfully*-----");
        }
        con.close();
    }

    void admin() throws Exception {
        System.out.print("ENTER PASSWORD:");
        int password = sc.nextInt();
        if (password != 1234) {
            System.out.println("-->Incorrect Password");
            System.out.println("-->Note:Only 5 Attempts");
            for (int i = 1; i < 5 && password != 1234; i++) {
                if (i >= 2) {
                    System.out.println("-->Incorrect Password");
                }
                System.out.print("ENTER PASSWORD:");
                password = sc.nextInt();
            }
        }
        if (password == 1234) {
            int n;
            do {
                System.out.println();
                adminFunction();
                System.out.println("Enter Your Choice");
                 n = sc.nextInt();
                switch (n) {
                    case 1:
                        System.out.println(registation());
                        break;
                    case 2:
                        serchStudent();
                        break;
                    case 3:
                        removestd();
                        break;
                    case 4:
                        addFees();
                        break;
                    case 5:
                        feesReceipt();
                        break;
                    case 6:
                        changeRoom();
                        break;
                    case 7:
                        StoreLeaveRecord();
                        break;
                    case 8:
                        DisplayLeaveRecord();
                        break;
                    case 9:
                        notPayFees();
                        break;
                    case 10:
                        clearDatabase();
                        break;
                    case 11:
                        n = 11;
                        break;
                    default:
                        System.out.println("----*Invalid choice. Please try again*----");
                }
            }while(n!=11);
        }
    }
    void clearDatabase() throws  Exception{
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hostel", "postgres", "1234");
        Statement st = con.createStatement();
        st.executeUpdate("truncate table student_details");
        st.executeUpdate("truncate table room");
        st.executeUpdate("truncate table leave");
        con.close();
    }
}


class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        hostel H = new hostel();
        int n;
        System.out.println("................................................");
        System.out.println("               HK's Hostel                      ");
        System.out.println("................................................");
        do {
            System.out.println();
            System.out.println("1....STUDENT RESISTAION");
            System.out.println("2....ADMIN PANEL");
            System.out.println("3....EXIT");
            System.out.print("Enter Your Choice:");
            n = sc.nextInt();
            if (n > 3) {
                for (int i = 1; n > 3; i++) {
                    System.out.println("------*!Invalid Chice!*------");
                    System.out.print("Enter Your Choice:");
                    n = sc.nextInt();
                }
            }
            switch (n) {
                case 1:
                    System.out.println(H.registation());
                    break;
                case 2:
                    H.admin();
                    break;
                case 3:
                    break;
            }
        } while (n != 3);
    }
}