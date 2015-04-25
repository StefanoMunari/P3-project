package server;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import algorithm.PuzzleSortInterface;
import algorithm.PuzzleSort;

import java.rmi.RemoteException;
import java.net.MalformedURLException;


class PuzzleSolverServer{

    private static String servername;

    public static void main(String[] args)
    {   
    try{
        if(args.length!=1)
            throw new InvalidArgumentException("EXCEPTION: the number of arguments is not valid");
        else
        {
            System.out.println("waiting for RMIregistry to start...");
            Thread.sleep(1500);
            System.out.println("creating the server...");
            
            servername= "rmi://localhost/"+args[0];
            PuzzleSortInterface dictionary= new PuzzleSort();
            Naming.rebind(servername,dictionary);

            System.out.println("The server "+servername+" is ready");
            }
        }
        catch(RemoteException e)
        {
            System.err.println("EXCEPTION: error while connecting to the registry");
            System.err.println(e.getMessage());
        }
        catch (MalformedURLException e)
        {
            System.err.println("EXCEPTION: the server URL is not valid");
            System.err.println(e.getMessage());
        }
        catch(InvalidArgumentException e)
        {
            System.err.println(e);
        }
        catch(InterruptedException e)
        {
            System.err.println("EXCEPTION: Server-thread interrupted");
            System.err.println(e.getMessage());
        }
        catch(Exception e)
        {
          System.err.println("EXCEPTION: Generic exception");
          System.err.println(e.getMessage());  
        }
     }
}