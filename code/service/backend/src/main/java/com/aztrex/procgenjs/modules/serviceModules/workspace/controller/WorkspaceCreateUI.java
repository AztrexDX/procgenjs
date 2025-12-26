
//
// import com.procgenfx.controller.base.control.bottom.BottomBaseControl;
// import com.procgenfx.controller.ui.workpsace.cell.WorkspaceUIListItemCell;
// import com.procgenfx.database.data.workspace.WorkspaceDao;
// import com.procgenfx.dto.data.workspace.WorkspaceConfig;
// import com.procgenfx.dto.data.workspace.item.WorkspaceUIListItem;
// import com.procgenfx.dto.state.AppState;
// import com.procgenfx.service.data.DataService;
// import com.procgenfx.service.data.workspace.WorkspaceService;
// import com.procgenfx.util.constants.FxmlPathConstant;
// import com.procgenfx.util.constants.MessageConstant;
// import com.procgenfx.util.service.DialogUtil;
// import com.procgenfx.util.service.MathUtil;
// import com.procgenfx.util.service.message.MessageHandler;
// import javafx.collections.ObservableList;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.*;
// import javafx.stage.DirectoryChooser;
// import javafx.stage.Stage;
// import javafx.util.Callback;
// import lombok.Setter;
//
// import java.io.File;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.Objects;
//
// public class WorkspaceCreateUI {
// private AppState appState = AppState.get();
//
// @FXML
// public TextField wsid;
// @FXML
// private TextField title;
// @FXML
// public TextField category;
// @FXML
// public TextField group;
// @FXML
// public TextField description;
// @FXML
// public TextField tags;
// @FXML
// public CheckBox selected;
// @FXML
// private TextField targetDirectoryField;
//
// @Setter
// private Callback<ListView<WorkspaceConfig>, ListCell<WorkspaceConfig>>
// listViewCellFactory;
//
// @FXML
// private ListView<WorkspaceUIListItem> workspaceListView = new ListView<>();
// private ObservableList<WorkspaceUIListItem> workspaceList;
// private static Stage stage;
// private static File selectedFolder;
// private DataService dataService;
// private WorkspaceService workspaceService;
// private WorkspaceDao workspaceDao;
// private BottomBaseControl bottomBaseControl;
//
// private WorkspaceUIListItem selectedWorkspaceItem;
//
//
// @FXML
// public void initialize() {
// workspaceDao = WorkspaceDao.get();
// workspaceService = WorkspaceService.get();
//
// bottomBaseControl = appState.getBottomBaseControl();
//
// workspaceListView.setCellFactory(e -> new WorkspaceUIListItemCell());
// populateWorkspaceListUI();
//// if (!workspaceService.getWorkspaceConfigMap().isEmpty()) {
//// workspaceListView.setItems(FXCollections.observableArrayList(workspaceService.getWorkspaceConfigMap().values()));
//// }
// }
//
// public void setUpPostConstruct() {
//
//
//// workspaceListView = new
// ListView<>(FXCollections.observableArrayList(workspaceService.getWorkspaceConfigMap().values()));
// }
//
// public void createWorkspace(ActionEvent actionEvent) {
// String targetDir = targetDirectoryField.getText().trim();
// String workspaceName = title.getText().trim();
//
// Path destinationPath = Paths.get(targetDir, workspaceName);
//
// if (Files.exists(destinationPath)) {
// MessageHandler.showAlertForType(MessageConstant.ERROR, null, "Workspace
// already exists.", Alert.AlertType.ERROR);
// return;
// }
//
// try {
// workspaceService.createWorkspace(targetDir, workspaceName, new
// WorkspaceUIListItem(wsid.getText(), workspaceName, Paths.get(targetDir,
// workspaceName).toAbsolutePath().toString(),
// category.getText(), group.getText(), description.getText(),
// MathUtil.tfTo10(selected.isSelected()), tags.getText()));
// populateWorkspaceListUI();
// MessageHandler.showAndClearAfterDelay(bottomBaseControl.getMessage8(),
// MessageConstant.COMPILED, 2);
// } catch (Exception e) {
// MessageHandler.showAlertForType(MessageConstant.ERROR, null, "Failed to
// create workspace: " + e.getMessage(), Alert.AlertType.ERROR);
// }
// }
//
// public void updateSelectedWorkspace(ActionEvent actionEvent) {
// if (Objects.nonNull(selectedWorkspaceItem)) {
// updateSelectedWorkspaceByFields();
// workspaceService.updateWorkspace(selectedWorkspaceItem);
// MessageHandler.showAndClearAfterDelay(bottomBaseControl.getMessage8(),
// MessageConstant.WORKSPACE_UPDATED, 2);
// } else {
// MessageHandler.showAlertForType(MessageConstant.ERROR, null,
// MessageConstant.NO_WORKSPACE_SELECTED, Alert.AlertType.ERROR);
// }
// }
//
// public void loadSelectedWorkspace(ActionEvent actionEvent) {
// selectedWorkspaceItem =
// workspaceListView.getSelectionModel().getSelectedItem();
// if (Objects.nonNull(selectedWorkspaceItem)) {
// // Convert 1/0 to true/false
// // Implement your workspace loading logic here
// updateFieldsBySelectedWorkspace();
// } else {
// MessageHandler.showAlertForType(MessageConstant.ERROR, null,
// MessageConstant.NO_WORKSPACE_SELECTED, Alert.AlertType.ERROR);
// }
// }
//
// public void removeSelectedWorkspace(ActionEvent actionEvent) {
// WorkspaceUIListItem selectedWorkspace =
// workspaceListView.getSelectionModel().getSelectedItem();
// workspaceService.removeWorkspace(selectedWorkspace);
// populateWorkspaceListUI();
// }
//
// public void chooseTargetDirectory(ActionEvent actionEvent) {
// DirectoryChooser chooser = new DirectoryChooser();
// chooser.setTitle("Select Folder Directory");
//
// selectedFolder = chooser.showDialog(stage);
// if (Objects.nonNull(selectedFolder)) {
// targetDirectoryField.setText(selectedFolder.getAbsolutePath());
// }
// }
//
// public static void showDialog(Stage owner) {
// DialogUtil.showItemAsDialog(WorkspaceCreateUI.class.getName(), owner,
// WorkspaceCreateUI.class.getResource(FxmlPathConstant.WorkSpaceUIPath));
// }
//
// private void populateWorkspaceListUI() {
// workspaceService.populateWorkspaceList();
// workspaceListView.setItems(workspaceService.getWorkspaceListFX());
// }
//
// private void updateFieldsBySelectedWorkspace() {
// wsid.setText(selectedWorkspaceItem.getWsid());
// title.setText(selectedWorkspaceItem.getTitle());
// category.setText(selectedWorkspaceItem.getCategory());
// group.setText(selectedWorkspaceItem.getGroup());
// description.setText(selectedWorkspaceItem.getDescription());
// tags.setText(selectedWorkspaceItem.getTags());
// targetDirectoryField.setText(selectedWorkspaceItem.getPath());
// selected.setSelected(MathUtil.tfFrom10(selectedWorkspaceItem.getSelected()));
// }
//
// private void updateSelectedWorkspaceByFields() {
// selectedWorkspaceItem.setWsid(wsid.getText());
// selectedWorkspaceItem.setTitle(title.getText());
// selectedWorkspaceItem.setCategory(category.getText());
// selectedWorkspaceItem.setGroup(group.getText());
// selectedWorkspaceItem.setDescription(description.getText());
// selectedWorkspaceItem.setTags(tags.getText());
// selectedWorkspaceItem.setPath(targetDirectoryField.getText());
// selectedWorkspaceItem.setSelected(MathUtil.tfTo10(selected.isSelected()));
// }
// }
