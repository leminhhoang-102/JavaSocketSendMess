package someting;

import someting.Asymetric.AsymmetricCryptography;

import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main
{

    public static void main(String[] args) throws Exception {
        AsymmetricCryptography ac = new AsymmetricCryptography();
        PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");
        PublicKey publicKey = ac.getPublic("KeyPair/publicKey");


        DataInputStream din = null;
        ServerSocket serverSocket = null;
        DataOutputStream dout = null;
        BufferedReader br = null;
        try
        {

            serverSocket = new ServerSocket(8086);
            System.out.println("Wait for client to say some thing ");

            Socket socket = serverSocket.accept();
            din = new DataInputStream(socket.getInputStream());

            OutputStream outputStream = socket.getOutputStream();
            dout = new DataOutputStream(outputStream);

            br = new BufferedReader(new InputStreamReader(System.in));

            String strFromClient = "", strToClient = "";
            while (!strFromClient.equals("stop"))
            {
                strFromClient = din.readUTF();
                String decrypted_msg = ac.decryptText(strFromClient, publicKey);
                System.out.println("client says: " + decrypted_msg);
                strToClient = br.readLine();
                dout.writeUTF(strToClient);
                dout.flush();
            }
        }
        catch (Exception exe)
        {
            exe.printStackTrace();
        }
        finally
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }

                if (din != null)
                {
                    din.close();
                }

                if (dout != null)
                {
                    dout.close();
                }
                if (serverSocket != null)
                {
                    /*
                     * closes the server socket.
                     */
                    serverSocket.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

}