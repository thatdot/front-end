package com.quine.language.server;

import com.quine.language.testclient.QuineLanguageClient;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class TestClientServer {
    public static void main(String[] args) {
        try {
            PipedInputStream inClient = new PipedInputStream();
            PipedOutputStream outClient = new PipedOutputStream();
            PipedInputStream inServer = new PipedInputStream();
            PipedOutputStream outServer = new PipedOutputStream();

            inClient.connect(outServer);
            outClient.connect(inServer);

            QuineLanguageServer server = new QuineLanguageServer();
            Launcher<LanguageClient> serverLauncher = LSPLauncher.createServerLauncher(server, inServer, outServer);
            Future<Void> serverListening = serverLauncher.startListening();

            QuineLanguageClient client = new QuineLanguageClient();
            Launcher<LanguageServer> clientLauncher = LSPLauncher.createClientLauncher(client, inClient, outClient);
            Future<Void> clientListening = clientLauncher.startListening();

            TextDocumentItem tdi = new TextDocumentItem();
            tdi.setText("MATCH (n)-[:]->(m) RETURN n.x, m.y");

            DidOpenTextDocumentParams dotdp = new DidOpenTextDocumentParams();
            dotdp.setTextDocument(tdi);

            clientLauncher.getRemoteProxy().getTextDocumentService().didOpen(dotdp);

            DocumentDiagnosticParams ddp = new DocumentDiagnosticParams();
            ddp.setTextDocument(new TextDocumentIdentifier("data/query1.quine"));

            CompletableFuture<DocumentDiagnosticReport> diagnosticsFuture = clientLauncher.getRemoteProxy().getTextDocumentService().diagnostic(ddp);

            System.out.println(diagnosticsFuture.join());

            CompletionParams p = new CompletionParams();
            p.setPosition(new Position(1, 13));
            p.setTextDocument(new TextDocumentIdentifier("data/query1.quine"));

            CompletableFuture<Either<List<CompletionItem>, CompletionList>> future = clientLauncher.getRemoteProxy().getTextDocumentService().completion(p);

            System.out.println(future.join());
        } catch (IOException ioe) {

        }
    }
}
