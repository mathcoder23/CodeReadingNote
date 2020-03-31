package jp.kitabatakep.intellij.plugins.codereadingrecorder;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import org.jdom.Element;

@State(
    name = AppConstants.appName,
    storages = {
        @Storage(AppConstants.appName + ".xml"),
    }
)
public class CodeReadingRecorderService implements PersistentStateComponent<Element>
{
    Project project;
    TopicList topicList;

    String lastExportDir = "";

    public CodeReadingRecorderService(@NotNull Project project)
    {
        this.project = project;
        topicList = new TopicList(project);
    }

    public static CodeReadingRecorderService getInstance(@NotNull Project project)
    {
        return ServiceManager.getService(project, CodeReadingRecorderService.class);
    }

    @Override
    public Element getState()
    {
        Element container = new Element(AppConstants.appName);
        container.addContent(topicList.getState());
        Element state = new Element("state");
        state.setAttribute("lastExportDir", lastExportDir != null ? lastExportDir : "");
        container.addContent(state);
        return container;
    }

    @Override
    public void loadState(@NotNull Element element)
    {
        topicList.loadState(element.getChild("topics"));
        Element stateElement = element.getChild("state");
        lastExportDir = stateElement.getAttributeValue("lastExportDir");
    }

    public TopicList getTopicList()
    {
        return this.topicList;
    }

    public String lastExportDir() { return lastExportDir != null ? lastExportDir : ""; }
    public void setLastExportDir(String lastExportDir) { this.lastExportDir = lastExportDir; }
}
