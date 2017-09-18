package ru.devcorvette.chat.guiclient;

import org.apache.log4j.Logger;
import ru.devcorvette.chat.core.ResourceManager;
import ru.devcorvette.chat.guiclient.actions.ActionsFactory;
import ru.devcorvette.chat.guiclient.messages.TabManager;
import ru.devcorvette.chat.guiclient.settings.SettingsPane;
import ru.devcorvette.chat.guiclient.users.UsersTree;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Главное окно программы.
 * Размещает все компонены отображения на панелях JPanel.
 */
public class ChatFrame extends JFrame {
    private static final Logger log = Logger.getLogger(ChatFrame.class);

    //задает системный Look and feel
    static {
        try {
            String laf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(laf);

        } catch (Exception e) {
            log.warn("Load the Look And Feel specified is failed:", e);
        }
    }

    private GUIClient client;
    private final JPanel mainPanel = new JPanel();
    private final JPanel statusPanel = new JPanel();
    private final JPanel upperPanel = new JPanel();
    private final JPanel toolsPanel = new JPanel();
    private final JPanel centralPanel = new JPanel();
    private SettingsPane settingsPane;
    private JTextPane entryField;
    private TabManager tabManager;
    private UsersTree usersTree;
    private SmilesMenu smilesMenu;
    private final Border raisedBevel = BorderFactory.createRaisedBevelBorder();

    public ChatFrame(String name, GUIClient client) {
        super(name);
        this.client = client;

        entryField = new JTextPane();
        tabManager = new TabManager(this, client);
        usersTree = new UsersTree(client, tabManager);
        smilesMenu = new SmilesMenu(entryField);
        settingsPane = new SettingsPane(this);

        initUpperPanel();
        initToolsPanel();
        initStatusPanel();
        initCentralPanel();
        initView();
    }

    /**
     * Собираем главное окно программы
     */
    protected void initView() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int height = (int) screenSize.getHeight() / 2;
        int width = (int) screenSize.getWidth() / 2;

        this.setSize(width, height);
        this.setIconImage(ResourceManager.getImage("chat.png").getImage());

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.setContentPane(mainPanel);

        ActionsFactory.initShowKeyMap(this);
        ActionsFactory.createChatKeyMap(mainPanel);
        ActionsFactory.createSendMessageHotKey(ActionsFactory.ENTER, entryField);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(centralPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    /**
     * Собираем компоненты на центральной панели
     */
    protected void initCentralPanel() {
        centralPanel.setBorder(raisedBevel);
        centralPanel.setLayout(new GridBagLayout());
        entryField.setBorder(BorderFactory.createLoweredBevelBorder());

        GridBag.addToPanel(centralPanel,
                tabManager,
                0, 0, 2, 1, 100, 100,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER);
        GridBag.addToPanel(centralPanel,
                new JScrollPane(usersTree),
                2, 0, 1, 3, 30, 50,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER);
        GridBag.addToPanel(centralPanel,
                toolsPanel,
                0, 1, 2, 1, 100, 0,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER);
        GridBag.addToPanel(centralPanel,
                new JScrollPane(entryField),
                0, 2, 1, 1, 100, 30,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER);
        GridBag.addToPanel(centralPanel,
                ButtonsFactory.initSendButton(client),
                1, 2, 1, 1, 0, 0,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER);
    }

    /**
     * Собираем компоненты на панели статуса
     */
    protected void initStatusPanel() {
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setBorder(raisedBevel);
        statusPanel.add(LabelsFactory.initStatusLabel());
        statusPanel.setMinimumSize(new Dimension(50, 50));
    }

    /**
     * Собираем компоненты на панели инструментов
     */
    //можно еще добавить кнопку передачи файлов да и много чего еще
    protected void initToolsPanel() {
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
        toolsPanel.setBorder(raisedBevel);
        toolsPanel.add(ButtonsFactory.initSmilesButton(smilesMenu));
    }

    /**
     * Собираем компоненты верхней панели
     */
    protected void initUpperPanel() {
        upperPanel.setLayout(new GridBagLayout());
        upperPanel.setBorder(raisedBevel);
        GridBag.addToPanel(upperPanel,
                ButtonsFactory.initConnectButton(this, client),
                0, 0, 1, 1, 0, 0,
                GridBagConstraints.BOTH,
                GridBagConstraints.WEST);

        GridBag.addToPanel(upperPanel,
                ButtonsFactory.initSettingsButton(settingsPane),
                1, 0, 1, 1, 0, 0,
                GridBagConstraints.BOTH,
                GridBagConstraints.WEST);

        GridBag.addToPanel(upperPanel,
                ButtonsFactory.initCreateNewRoomButton(client),
                2, 0, 1, 1, 0, 0,
                GridBagConstraints.BOTH,
                GridBagConstraints.WEST);

        GridBag.addToPanel(upperPanel,
                new JPanel(),
                3, 0, 1, 1, 10, 10,
                GridBagConstraints.BOTH,
                GridBagConstraints.WEST);

        GridBag.addToPanel(upperPanel,
                LabelsFactory.getUserName(client.getOwnName()),
                4, 0, 1, 1, 3, 3,
                GridBagConstraints.REMAINDER,
                GridBagConstraints.CENTER);
    }

    /*Диаолговые окна*/

    /**
     * Диалоговое окно, для получения информации от пользователя
     */
    public String getInformationFromUser(String question) {
        String reply = JOptionPane.showInputDialog(
                this,
                question,
                this.getTitle(),
                JOptionPane.QUESTION_MESSAGE);

        if (reply == null)
            return null;

        return reply;
    }

    /**
     * Выводит диалоговое окно с сообщением message
     * и ставит фокус на поле ввода
     */
    public void showInformMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Информация",
                JOptionPane.INFORMATION_MESSAGE);
        setFocus();
    }

    /**
     * Выводит диалоговое окно с ошибкой и сообщением message
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }

    public void setFocus() {
        entryField.requestFocus(true);
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }

    public JPanel getToolsPanel() {
        return toolsPanel;
    }

    public JPanel getUpperPanel() {
        return upperPanel;
    }

    public JPanel getCentralPanel() {
        return centralPanel;
    }

    public JTextPane getEntryField() {
        return entryField;
    }

    public void setEntryField(JTextPane entryField) {
        this.entryField = entryField;
    }

    public TabManager getTabManager() {
        return tabManager;
    }

    public void setTabManager(TabManager tabManager) {
        this.tabManager = tabManager;
    }

    public UsersTree getUsersTree() {
        return usersTree;
    }

    public void setUsersTree(UsersTree usersTree) {
        this.usersTree = usersTree;
    }

    public SmilesMenu getSmilesMenu() {
        return smilesMenu;
    }

    public void setSmilesMenu(SmilesMenu smilesMenu) {
        this.smilesMenu = smilesMenu;
    }

    public SettingsPane getSettingsPane() {
        return settingsPane;
    }

    public void setSettingsPane(SettingsPane settingsPane) {
        this.settingsPane = settingsPane;
    }
}
