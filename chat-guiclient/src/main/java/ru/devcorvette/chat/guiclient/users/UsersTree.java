package ru.devcorvette.chat.guiclient.users;

import ru.devcorvette.chat.core.ResourceManager;
import ru.devcorvette.chat.core.Client;
import ru.devcorvette.chat.guiclient.messages.TabManager;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.*;

/**
 * Глвавный класс дерева пользователей.
 * Инициализирует и собирает компоненты дерева.
 * <p>
 * Содержит главный метод обновления узлов чат румов.
 * <p>
 * Разворачивает/Сворачивает ветви и запоминает какие
 * развернул/свернул пользователь.
 * <p>
 * Взаимодействует с вкладками TabManager
 */
public class UsersTree extends JTree {
    private final Map<String, RoomNode> roomsMap = new HashMap<>();
    private final Client client;
    private final TabManager tabManager;

    private final DefaultMutableTreeNode root;
    private final DefaultTreeModel model;

    private final List<TreePath> expandedPaths;
    private final List<TreePath> copyPaths;

    private String activeNode;

    public UsersTree(Client client, TabManager tabManager) {
        this.client = client;
        this.tabManager = tabManager;

        root = new DefaultMutableTreeNode("root");
        model = new DefaultTreeModel(root);

        expandedPaths = new ArrayList<>();
        copyPaths = new ArrayList<>();

        initUsersTree();
    }

    /**
     * Задаем свойства пользовательского дерева
     * и добвляем слушателей событий.
     */
    private void initUsersTree() {
        this.setEditable(false);
        this.setModel(model);
        this.setRootVisible(false);
        this.scrollPathToVisible(new TreePath(root.getPath()));

        DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
        render.setOpenIcon(ResourceManager.getImage("chatRoom16.png"));
        render.setClosedIcon(ResourceManager.getImage("chatRoom16.png"));
        render.setLeafIcon(ResourceManager.getImage("men.png"));
        this.setCellRenderer(render);

        this.addMouseListener(new TreeMouseListener(this));
        this.addTreeExpansionListener(new ExpansionListener());

        MenuFactory.initRoomMenu(client, this);
        MenuFactory.initUserMenu(this);
    }

    /**
     * Добавляет или обновляет чат рум со списком пользователей
     * Обновляет отображение UsersTree и разварачивает узлы которые были развернуты ранее
     */
    public synchronized void changeRoom(String[] usersArray, String roomName) {
        //создание нового чат рум
        if (!roomsMap.containsKey(roomName))
            roomsMap.put(roomName, new RoomNode(this, root, roomName));

        //удаление пустого чат рум
        if (usersArray.length == 0) {
            roomsMap.get(roomName).selfRemove();
            roomsMap.remove(roomName);
        }
        //обновление
        else {
            roomsMap.get(roomName).changeUsers(Arrays.asList(usersArray));
        }

        //разворачивает все развернутые ранее узлы
        copyPaths.clear();
        copyPaths.addAll(expandedPaths);
        expandedPaths.clear();
        model.nodeStructureChanged(root);
        for (TreePath p : copyPaths) {
            this.expandPath(p);
        }
    }

    /**
     * Очищает дерево пользователей
     */
    public synchronized void cleanUsersThree() {
        root.removeAllChildren();
        roomsMap.clear();
        model.nodeStructureChanged(root);
    }

    /**
     * Разворачивает ветку чат рума
     */
    public synchronized void expandRoom(String roomName) {
        if (!roomsMap.containsKey(roomName)) return;

        expandPath(new TreePath(roomsMap.get(roomName).getPath()));
    }

    /**
     * Открывает приватный чат через TabManager
     */
    public void openPrivateRoom(String roomName) {
        tabManager.addMessagesTab(roomName, TabManager.PRIVATE_TAB, true);
    }

    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    public String getOwnName() {
        return client.getOwnName();
    }

    public String getMainRoomName() {
        return client.getMainRoomName();
    }

    public void connectToRoom(String roomName) {
        client.connectToRoom(roomName);
    }

    public String getActiveNode() {
        return activeNode;
    }

    public void setActiveNode(String activeNode) {
        this.activeNode = activeNode;
    }

    public Map<String, RoomNode> getRoomsMap() {
        return Collections.unmodifiableMap(roomsMap);
    }

    /**
     * Добавляет/Удаляет узел в список развернутых пользователем узлов
     */
    private class ExpansionListener implements TreeExpansionListener {
        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            synchronized (UsersTree.this) {
                expandedPaths.add(event.getPath());
            }
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            synchronized (UsersTree.this) {
                expandedPaths.remove(event.getPath());
            }
        }
    }
}