//package com.quine.language.server
//
//import org.eclipse.lsp4j.{InitializeParams, InitializeResult, InitializedParams, SetTraceParams, WorkDoneProgressCancelParams}
//import org.eclipse.lsp4j.services.{LanguageServer, NotebookDocumentService, TextDocumentService, WorkspaceService}
//
//import java.util.concurrent.CompletableFuture
//
//class QuineLanguageServer extends LanguageServer {
//  override def initialized(params: InitializedParams): Unit = ???
//
//  override def initialized(): Unit = ???
//
//  override def getNotebookDocumentService: NotebookDocumentService = super.getNotebookDocumentService
//
//  override def cancelProgress(params: WorkDoneProgressCancelParams): Unit = ???
//
//  override def setTrace(params: SetTraceParams): Unit = ???
//
//  override def initialize(params: InitializeParams): CompletableFuture[InitializeResult] = ???
//
//  override def shutdown(): CompletableFuture[AnyRef] = ???
//
//  override def exit(): Unit = ???
//
//  override def getTextDocumentService: TextDocumentService = new QuineTextDocumentService
//
//  override def getWorkspaceService: WorkspaceService = ???
//}
