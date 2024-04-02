package DictionaryServer;

public class Server {
    private static int port;
    private static
    public static void main(String[] args)
    {
        if (args.length != 0)
        {
            System.out.println("Missing arguments");
            System.exit(1);
        }
        else
        {
            port = Integer.parseInt(args[0]);

        }
    }
}
