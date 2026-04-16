package IRANZO;
import java.util.*;

public class UI_PaymentProcess {
    public static void main(String[] args) {
        Scanner ui = new Scanner (System.in);
        String modePayment = """
                             How would you like to pay today?
                             ============================
                             A | Card Payment (e.g. Mastercard, Visa)
                             B | Online Payment (GCASH ONLY)
                             C | Cash Payment
                             X | Cancel
                             ============================
                             """;
        System.out.print("ENTER MODE OF PAYMENT: ");
            char modePaymentInput = ui.next().charAt(0);
            
        switch (modePaymentInput)
        {
            case 'A':
                cardPayment(); break;
            case 'B':
                onlinePayment(); break;
            case 'C':
                cashPayment(); break;
            case 'X':
                System.out.println("Have a Great Day! Exiting Program...");
                System.exit(0); break;
            default:
                System.err.println("INVALID INPUT.");
        }
    }   
    
    public static void cardPayment()
    {
        
    }
    
    public static void onlinePayment()
    {
        
    }
    
    public static void cashPayment()
    {
        
    }
}
