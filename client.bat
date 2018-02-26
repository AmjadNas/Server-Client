@cd %cd%\src
@for /r %%a in (*.java) do @javac %%a
@java View.Client_Server
@pause