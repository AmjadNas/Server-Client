@cd %cd%\src
@for /r %%a in (*.java) do @javac %%a
@java Model.Server
@pause