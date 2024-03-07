package com.quine.language.server;

import com.quine.cypher.ParseError;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class QuineTextDocumentService implements TextDocumentService {

    private ContextAwareLanguageService cals;

    private String currentQuery;

    public QuineTextDocumentService(ContextAwareLanguageService cals) {
        super();
        this.cals = cals;
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        currentQuery = params.getTextDocument().getText();
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {

    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {

    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {

    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        List<CompletionItem> completionItems = new ArrayList<>();

        List<String> completions = cals.edgeCompletions("");
        for(int i = 0; i < completions.size(); i++) {
            String suggestion = completions.get(i);
            CompletionItem ci = new CompletionItem();
            ci.setInsertText(suggestion);
            completionItems.add(ci);
        }
        return CompletableFuture.supplyAsync(() -> Either.forLeft(completionItems));
    }

    @Override
    public CompletableFuture<DocumentDiagnosticReport> diagnostic(DocumentDiagnosticParams params) {
        List<ParseError> errors = cals.parseErrors(currentQuery);
        List<Diagnostic> diagnostics = new ArrayList<>();
        for(int i = 0; i < errors.size(); i++) {
            ParseError err = errors.get(i);

            Range r = new Range();
            //r.setStart();

            Diagnostic d = new Diagnostic();
            d.setRange(r);
            d.setMessage(err.message());
            diagnostics.add(d);
        }

        RelatedFullDocumentDiagnosticReport rfddr = new RelatedFullDocumentDiagnosticReport();
        rfddr.setItems(diagnostics);
        DocumentDiagnosticReport ddr = new DocumentDiagnosticReport(rfddr);
        return CompletableFuture.supplyAsync(() -> ddr);
    }
}
