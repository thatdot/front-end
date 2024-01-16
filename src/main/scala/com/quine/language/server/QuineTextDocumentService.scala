//package com.quine.language.server
//
//import org.eclipse.lsp4j.{DidChangeTextDocumentParams, DidCloseTextDocumentParams, DidOpenTextDocumentParams, DidSaveTextDocumentParams}
//import org.eclipse.lsp4j.services.TextDocumentService
//
//class QuineTextDocumentService extends TextDocumentService {
//
//
//  override def didOpen(params: DidOpenTextDocumentParams): Unit = println(params.getTextDocument.getText)
//
//  override def didChange(params: DidChangeTextDocumentParams): Unit = ???
//
//  override def didClose(params: DidCloseTextDocumentParams): Unit = ???
//
//  override def didSave(params: DidSaveTextDocumentParams): Unit = ???
//}
