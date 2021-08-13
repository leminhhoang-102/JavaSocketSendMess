package someting;

import someting.Asymetric.AsymmetricCryptography;

import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Client
{
    public static void main(String[] args) throws Exception {
        AsymmetricCryptography ac = new AsymmetricCryptography();
        PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");
        PublicKey publicKey = ac.getPublic("KeyPair/publicKey");

        Socket socket = null;
        DataInputStream din = null;
        DataOutputStream dout = null;
        BufferedReader br = null;

        try
        {
            socket = new Socket("localhost", 8086);
            din = new DataInputStream(socket.getInputStream());

            OutputStream outputStream = socket.getOutputStream();
            dout = new DataOutputStream(outputStream);

            br = new BufferedReader(new InputStreamReader(System.in));

            String strFromServer = "", strToClient = "";
            while (!strFromServer.equals("stop"))
            {
                strFromServer = br.readLine();

                String encrypted_msg = ac.encryptText(strFromServer, privateKey);
                dout.writeUTF(encrypted_msg);
                dout.flush();
                strToClient = din.readUTF();
                String decrypted_msg = ac.decryptText(strToClient, publicKey);
                System.out.println("Server says: " + decrypted_msg);
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
                if (socket != null)
                {
                    /*
                     * closes this socket
                     */
                    socket.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}