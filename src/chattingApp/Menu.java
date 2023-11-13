package chattingApp;

import javax.swing.*;

public class Menu extends JPopupMenu {
    public Menu() {
        createMenu();
    }

    private void createMenu() {
        JMenuItem profileMenuItem = new JMenuItem("Profile");
        JMenuItem settingsMenuItem = new JMenuItem("Settings");

        JMenu moreMenu = new JMenu("More");
        JMenuItem starredMsg = new JMenuItem("Starred Messages");
        JMenuItem blackList = new JMenuItem("Blacked List");

        moreMenu.add(starredMsg);
        moreMenu.add(blackList);

        add(profileMenuItem);
        add(settingsMenuItem);
        add(moreMenu);

        // Using lambda expressions for action listeners
        profileMenuItem.addActionListener(e -> handleProfileClick());
        settingsMenuItem.addActionListener(e -> handleSettingsClick());
        starredMsg.addActionListener(e -> handleItem1Click());
        blackList.addActionListener(e -> handleItem2Click());
    }

    private void handleProfileClick() {
        // Implement the profile functionality here
    }

    private void handleSettingsClick() {
        // Implement the settings functionality here
    }

    private void handleItem1Click() {
        // Implement the starred messages  functionality here
    }

    private void handleItem2Click() {
        // Implement the blacklist  functionality here
    }
}
