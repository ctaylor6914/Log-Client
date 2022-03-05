/*	FILE			: Client.java
    PROGRAMMER		: Mita Das & Colby Taylor
    FIRST VERSION	: 2022-02-15
    DESCRIPTION		: This is the client program which sends data to server
                    : the data can be chosen or created by the user
                    : this client is used as a test platform for the
                    : corresponding server
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.Scanner;



/*	CLASS			: Client
    DESCRIPTION		: This class is the entry point of the program where logs will be generated and send to server
*/
public class Client
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        int input = 0;
        String sendMsg = "";
        String inputMsg = "";
        int message_counter = 0;

        // Randomly generating the client id
        String clientId = String.valueOf((int) ((Math.random() * (10000 - 1)) + 1));

        boolean on = true;
        while(on)
        {
            //print the menu items for testing
            System.out.print("----- Please Select Test -----\n");
            System.out.print("  1 - Simple Log Test \n");
            System.out.print("  2 - Simple Log Test Error \n");
            System.out.print("  3 - Noisy Log Test Client Test \n");
            System.out.print("  4 - Manual Log Entry Test \n");
            System.out.print("  5 - Invalid Log Level Test \n");
            System.out.print("  6 - Invalid Client ID Test \n");
            System.out.print("  7 - Enter whatever you want - probably and error \n");
            System.out.print("  8 - Quit \n");
            System.out.print(" Please Enter Option: ");

            // Getting the option input
            try{
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Get the time for the log
            Instant instant = Instant.now();
            String timeNow = instant.toString();

            if(input < 1 || input > 8) //if input is not within range
            {
                System.out.println("\nError: Incorrect Option, please try again.\n");
            }
            else if(input == 1) //simple log test
            {
                LogMessage logMessage = new LogMessage("1", timeNow, clientId, "Successful operation completed");

                sendMsg = logMessage.getMessage();
                message_counter = 1;
            }
            else if (input == 2)// simple log error test
            {
                // Performing numeric overflow exception
                String errorString = "";
                try{
                    int a = 10/0;
                }
                catch(Exception e)
                {
                    errorString = e.toString();
                }

                LogMessage logMessage = new LogMessage("2", timeNow, clientId, errorString);

                sendMsg = logMessage.getMessage();
                message_counter = 1;
            }
            else if(input == 3) // simple log error test as a noisy client sending 100 log messages
            {
                //Performing array out of bound exception
                String errorString = "";
                int[] arr = new int[5];
                try{
                    arr[6] = 10;
                }
                catch(Exception e)
                {
                    errorString = e.toString();
                }
                LogMessage logMessage = new LogMessage("3", timeNow, clientId, errorString);
                sendMsg = logMessage.getMessage();
                message_counter = 100;
            }
            else if(input == 4)// manual log entry with proper log header format
            {
                // Manual log entry
                System.out.print("\nPlease enter your log message: ");
                inputMsg = scanner.nextLine();

                LogMessage logMessage = new LogMessage("4", timeNow, clientId, inputMsg);

                sendMsg = logMessage.getMessage();
                message_counter = 1;
            }
            else if(input == 5)// logLevel error test log level is out of range
            {
                // Invalid log entry
                LogMessage logMessage = new LogMessage("14", timeNow, clientId, "Invalid log level for test");

                sendMsg = logMessage.getMessage();
                message_counter = 1;
            }
            else if(input == 6) //invalid client id test
            {
                // Invalid client id entry
                LogMessage logMessage = new LogMessage("8", timeNow, "100000", "Invalid client id for test");

                sendMsg = logMessage.getMessage();
                message_counter = 1;
            }
            else if(input == 7)// will only send what is input
            {
                inputMsg = scanner.nextLine();

                sendMsg = inputMsg;
                message_counter = 1;
            }
            else if(input == 8)// quit option
            {
                System.out.println("\nProgram Quit\n");
                on = false;
                break;
            }

            while(message_counter > 0)
            {
                try {
                    Socket soc = new Socket(args[0], 30000);                           //setup socket
                    DataOutputStream out = new DataOutputStream(soc.getOutputStream());     //create out stream
                    DataInputStream in = new DataInputStream(soc.getInputStream());         //create in stream

                    out.writeUTF(sendMsg);                                                  //send out message
                    System.out.print("\nTo server   : " + sendMsg);                         //print sent message to screen

                    String msg = (String) in.readUTF();                                     // receive in string
                    System.out.println("\nFrom server : " + msg+"\n");                      //print return message to screen

                    out.flush();                                                            //flush socket buffer
                    out.close();                                                            //close out stream
                    soc.close();                                                            //close socket

                } catch (Exception e) {
                    e.printStackTrace();                                                    //if any errors record and print
                }

                message_counter--;                                                          //decrease message counter
            }
        }
    }
}

/*	CLASS			: LogMessage
    DESCRIPTION		: This class is for making log message configurable
*/
class LogMessage
{
    String logID;
    String time;
    String clientID;
    String message;

    /*
        FUNCTION		: Constructor
        DESCRIPTION		: This is the user defined constructor
        PARAMETERS		: String logID, String time, String clientID, String message
        RETURNS         : none
    */
    LogMessage(String logID, String time, String clientID, String message)
    {
        this.logID = logID;
        this.time = time;
        this.clientID = clientID;
        this.message = message;
    }

    /*
        FUNCTION		: getMessage
        DESCRIPTION		: The function returns the message
        PARAMETERS		: void
        RETURNS         : String : Returns generated log message
    */
    public String getMessage()
    {
        return logID + "_" + time + "_" + clientID + "_" + message;
    }
}
