
import java.util.InputMismatchException;
import java.util.Scanner;
class GymnasticsGymAPI {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in); 
        boolean exit = false;
        
        while(!exit) {
            System.out.println("Enter integer to choose action from menu:\n 1) Create \n" 
                            + " 2) Update\n 3) Schedule\n" 
                            + " 4) list \n 5) Get\n 6) Notification\n 7) Exit");

           
           int action = 0;
            try {
                action = in.nextInt();
            }
            catch(InputMismatchException input){
                System.err.println("Integers only.");
                in.next();
            }
            

            switch (action) {
                case 1 : 
                    create(in);
                    break;
                case 2 : 
                    update(in);
                    break;
                case 3 : 
                    schedule(in);
                    break;
                case 4 : 
                    list(in);
                    break;
                case 5 : 
                    get(in);
                    break;
                case 6 : 
                    notification(in);
                    break;
                case 7 : 
                    exit = true;
                    System.out.println("System exiting. Thank you!");
                    break;
                default:
                    System.err.println("Please enter a valid value.");
            }
            

        }
        in.close();
    }

    public static void create(Scanner in){
        System.out.println("Enter integer to choose enitity to create:\n " +
                            "1) classes\n 2) Students\n 3) Coaches\n " +
                            "4) Emergency contact\n 5) Coach Schedule");  
    }

    public static void update(Scanner in){

    }

    public static void schedule(Scanner in){

    }

    public static void list(Scanner in){

    }

    public static void get(Scanner in){

    }

    public static void notification(Scanner in){

    }
}
