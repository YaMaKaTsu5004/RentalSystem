import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class GUI extends JFrame{

    public static ArrayList<String> log = new ArrayList<>();

    /* Adminstar Buttons */

    private JButton usersBtn;
    private JButton cus_newButton;
    private JButton cus_removeButton;
    private JButton inv_newButton;
    private JButton inv_addButton;
    private JButton inv_removeButton;

    ///////////////////////

    private JTextField login_userNameField;
    private JPasswordField login_passwordField;
    private static String nowLoginUserName;
    private static String nowLoginPassword;
    private static boolean isNowLoginAdmin;

    private JLabel northLabel;

    private JPasswordField cpass_currentPasswordField;
    private JPasswordField cpass_newPasswordField;
    private JPasswordField cpass_confirmNewPasswordField;


    private String nowCardPanel = "";

    private JPanel cardPanel = new JPanel();
    private CardLayout layout = new CardLayout();
    private JPanel leftPanel;
    private DefaultTableModel invTableModel;
    private JTable invTable;
    private DefaultTableModel cusTableModel;
    private JTable cusTable;
    private DefaultTableModel worTableModel;
    private JTable worTable;
    private DefaultTableModel invOpen_cusTableModel;
    private JTable invOpen_cusTable;
    private DefaultTableModel invOpen_invTableModel;
    private JTable invOpen_invTable;
    private DefaultTableModel cusOpen_cusTableModel;
    private JTable cusOpen_cusTable;
    private DefaultTableModel cusOpen_invTableModel;
    private JTable cusOpen_invTable;


    private JTextField invNew_nameField = new JTextField();
    private DefaultComboBoxModel<Integer> defaultComboBoxModel = new DefaultComboBoxModel<Integer>();

    private JComboBox<Integer> inv_addQuantityComboBox = new JComboBox<Integer>(defaultComboBoxModel);
    private JComboBox<Integer> inv_removeQuantityComboBox = new JComboBox<Integer>(defaultComboBoxModel);
    private JComboBox<Integer> invNew_quantityComboBox = new JComboBox<Integer>(defaultComboBoxModel);

    private JTextField cusNew_nameField = new JTextField();
    private DefaultComboBoxModel<Integer> cusNew_ageComboBoxModel = new DefaultComboBoxModel<Integer>();
    private DefaultComboBoxModel<String> cusNew_genderComboBoxModel = new DefaultComboBoxModel<String>();
    private DefaultComboBoxModel<Integer> cusNew_yearComboBoxModel = new DefaultComboBoxModel<Integer>();
    private DefaultComboBoxModel<Integer> cusNew_monthComboBoxModel = new DefaultComboBoxModel<Integer>();
    private DefaultComboBoxModel<Integer> cusNew_dayComboBoxModel = new DefaultComboBoxModel<Integer>();
    private JComboBox<Integer> cusNew_ageComboBox = new JComboBox<Integer>(cusNew_ageComboBoxModel);
    private JComboBox<String> cusNew_genderComboBox = new JComboBox<String>(cusNew_genderComboBoxModel);
    private JComboBox<Integer> cusNew_yearComboBox = new JComboBox<Integer>(cusNew_yearComboBoxModel);
    private JComboBox<Integer> cusNew_monthComboBox = new JComboBox<Integer>(cusNew_monthComboBoxModel);
    private JComboBox<Integer> cusNew_dayComboBox = new JComboBox<Integer>(cusNew_dayComboBoxModel);

    private JTextField worNew_nameField = new JTextField();

    private JButton cus_openButton;
    private String cus_tableAllField = "";

    private JButton inv_openButton;
    private String inv_furnitureName = "";

    private int cusOpen_customerIndex;
    private int invOpen_furnitureIndex;


    class CardChangeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            
            if(cmd.equals("home")){
                leftPanel.setVisible(false);
            }else{
                leftPanel.setVisible(true);
            }

            switch(cmd){
                case "inventory":
                    northLabel.setText("HOME > INVENTORY");
                    break;
                case "customers":
                    northLabel.setText("HOME > CUSTOMERS");
                    break;
                case "users":
                    northLabel.setText("HOME > USERS");
                    break;
                case "inv_newPanel":
                    northLabel.setText("HOME > INVENTORY > NEW FURNITURE");
                    break;
                case "cus_newPanel":
                    northLabel.setText("HOME > CUSTOMERS > NEW CUSTOMERS");
                    break;
                case "wor_newPanel":
                    northLabel.setText("HOME > USERS > NEW ADMIN USER");
                    break;
                case "changePassword":
                    northLabel.setText("HOME > CHANGE PASSWORD");
                    break;
                default:

            }
            nowCardPanel = cmd;
            layout.show(cardPanel,cmd);

        }
    }

    class LoginListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String userName = login_userNameField.getText();
            String password = new String(login_passwordField.getPassword());

            for(int i = 0; i < RentalSystem.users.size(); i++){
                User worker = RentalSystem.users.get(i);
                if(worker.getName().equals(userName) && worker.getPassword().equals(password)){
                    layout.show(cardPanel, "home");
                    if(!worker.getAdminster()){
                        usersBtn.setEnabled(false);
                        cus_newButton.setEnabled(false);
                        cus_removeButton.setEnabled(false);
                        inv_newButton.setEnabled(false);
                        inv_addButton.setEnabled(false);
                        inv_removeButton.setEnabled(false);
                    }
                    
                    
                    nowLoginUserName = userName;
                    nowLoginPassword = password;
                    isNowLoginAdmin = worker.getAdminster();
                    cusTableView();
                    northLabel.setText("HOME");
                    log.add(getTime() + "   " + userName + " Login");
                    break;
                }

                if(i + 1 == RentalSystem.users.size()){
                    JOptionPane.showMessageDialog(null, "Wrong username or password", "Error" ,JOptionPane.ERROR_MESSAGE);
                }
            }
            
        }

    }

    class NewFurnitureListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = invNew_nameField.getText();
            if(!name.isEmpty()){
                int quantity = (int)invNew_quantityComboBox.getSelectedItem();

                int furnitureCountsSize = Furniture.furnitureCounts.size();
                if(name != ""){
                    for(int i = 0; i < furnitureCountsSize; i++){
                        if(Furniture.furnitureCounts.get(i).getName().equals(name)){
                            JOptionPane.showMessageDialog(null, "this item is already in table", "Error" ,JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        if(Furniture.furnitureCounts.size() == i + 1){
                            for(int j = 1; j <= quantity; j++){
                                if(j == 1){
                                    RentalSystem.furnitures.add(new Furniture(name, 1, false));
                                }else{
                                    RentalSystem.furnitures.add(new Furniture(name, 1, true));
                                }
                            }
                            log.add(getTime() + "   " + nowLoginUserName + " NewFurniture " + name + " " + quantity);
                            invTableView();
                            nowCardPanel = "inventory";
                            layout.show(cardPanel,"inventory");
                        }
                    }

                    if(Furniture.furnitureCounts.size() == 0){
                        for(int j = 1; j <= quantity; j++){
                            if(j == 1){
                                RentalSystem.furnitures.add(new Furniture(name, 1, false));
                            }else{
                                RentalSystem.furnitures.add(new Furniture(name, 1, true));
                            }
                        }
                        log.add(getTime() + "   " + nowLoginUserName + " NewFurniture " + name + " " + quantity);
                        invTableView();
                        nowCardPanel = "inventory";
                        layout.show(cardPanel,"inventory");
                    }
                }
            }
        } 
    }

    class AddFurnitureListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(invTable.getSelectedRow() >= 0){
                int addQuantity = (int)inv_addQuantityComboBox.getSelectedItem();
                int rowIndex = invTable.convertRowIndexToModel(invTable.getSelectedRow());
                String selectedFurnitureName = invTableModel.getValueAt(rowIndex, 0).toString();
                for(int i = 0; i < Furniture.furnitureCounts.size(); i++){
                    if(Furniture.furnitureCounts.get(i).getName().equals(selectedFurnitureName)){
                        for(int j = 1; j <= addQuantity; j++){
                            RentalSystem.furnitures.add(new Furniture(selectedFurnitureName, 1, true));
                        }
                        invTableModel.setValueAt(Furniture.furnitureCounts.get(i).getCount(), rowIndex, 1);
                        log.add(getTime() + "   " + nowLoginUserName + " AddFurniture " + selectedFurnitureName + " " + addQuantity);
                        break;
                    }
                }
            }
        }
    }

    class RemoveFurnitureListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(invTable.getSelectedRow() >= 0){
                int removeCount = 0;
                int removeQuantity = (int)inv_removeQuantityComboBox.getSelectedItem();
                int loopCount;
                int rowIndex = invTable.convertRowIndexToModel(invTable.getSelectedRow());
                String selectedFurnitureName = invTableModel.getValueAt(rowIndex, 0).toString();
                int FurnitureCountIndex = -1;
                for(int i = 0; i < Furniture.furnitureCounts.size(); i++){
                    if(Furniture.furnitureCounts.get(i).getName().equals(selectedFurnitureName)) {
                        FurnitureCountIndex = i; 
                        break;
                    }
                }
                if(FurnitureCountIndex != -1){
                    if(Furniture.furnitureCounts.get(FurnitureCountIndex).getCount() >= removeQuantity ){
                        loopCount = removeQuantity;
                    }else{
                        loopCount = Furniture.furnitureCounts.get(FurnitureCountIndex).getCount();
                    }

                    for(int i = RentalSystem.furnitures.size() - 1; i >= 0 && removeCount < loopCount; i--){
                        if(RentalSystem.furnitures.get(i).getName().equals(selectedFurnitureName) && RentalSystem.furnitures.get(i).getCustomer() == null){
                            RentalSystem.furnitures.remove(i);
                            removeCount++;
                        }
                    }
                    Furniture.furnitureCounts.get(FurnitureCountIndex).setCount(-1 * removeCount);
                    log.add(getTime() + "   " + nowLoginUserName + " AddFurniture " + selectedFurnitureName + " " + --removeCount);
                }
                invTableView();
            }
        }
    }

    class InventoryOpenListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {   
            invOpenTableView();
            northLabel.setText("HOME > INVENTORY > OPEN " + inv_furnitureName);
        }
    }

    class InventoryTableSelectedListener implements ListSelectionListener{
        
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(invTable.getSelectedRow() >= 0){
                inv_openButton.setEnabled(true);
                int rowIndex = invTable.convertRowIndexToModel(invTable.getSelectedRow());
                inv_furnitureName = (String)invTableModel.getValueAt(rowIndex, 0);
            }
        }
        
    }

    class InventoryRendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int keepRow = invOpen_cusTable.getSelectedRow();
            if(!(keepRow < 0)){
                int rowIndex = invOpen_cusTable.convertRowIndexToModel(invOpen_cusTable.getSelectedRow());
                String inv_tableAllField = (String)invOpen_cusTableModel.getValueAt(rowIndex, 0) + "," + (int) invOpen_cusTableModel.getValueAt(rowIndex, 1) + "," + (String)invOpen_cusTableModel.getValueAt(rowIndex, 2) + "," + (String)invOpen_cusTableModel.getValueAt(rowIndex,3);
                for(int i = 0; i < RentalSystem.customers.size(); i++){
                    if(RentalSystem.customers.get(i).getAllField().equals(inv_tableAllField)){
                        for(int j = 0; j < RentalSystem.furnitures.size(); j++){
                            if(RentalSystem.furnitures.get(j).getName().equals(inv_furnitureName) && RentalSystem.furnitures.get(j).getCustomer() == null){
                                RentalSystem.furnitures.get(j).setCustomer(RentalSystem.customers.get(i));
                                RentalSystem.customers.get(i).addCustomerFurniture(RentalSystem.furnitures.get(j));
                                log.add(getTime() + "   " + nowLoginUserName + " Rend " + inv_furnitureName + " to " + RentalSystem.customers.get(i).getAllField());
                                break;
                            }
                            
                        }
                        break;
                    }
                }

                invOpenTableView();
                invOpen_cusTable.setRowSelectionInterval(keepRow, keepRow);
            }
        }
    }

    class InventoryReturnListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int keepRow = invOpen_invTable.getSelectedRow();
            if(!(keepRow < 0)){
                int customerIndex = -1;
                int rowIndex = invOpen_invTable.convertRowIndexToModel(invOpen_invTable.getSelectedRow());
                if(!(((String)invOpen_invTableModel.getValueAt(rowIndex, 0)).isEmpty())){
                String inv_tableAllField = (String)invOpen_invTableModel.getValueAt(rowIndex, 0) + "," + (int) invOpen_invTableModel.getValueAt(rowIndex, 1) + "," + (String)invOpen_invTableModel.getValueAt(rowIndex, 2) + "," + (String)invOpen_invTableModel.getValueAt(rowIndex,3);
                for(int i = 0; i < RentalSystem.customers.size(); i++){
                    if(RentalSystem.customers.get(i).getAllField().equals(inv_tableAllField)){
                        customerIndex = i;
                        break;
                    }
                }

                for(int i = 0; i < RentalSystem.furnitures.size(); i++){
                    if(RentalSystem.furnitures.get(i).getName().equals(inv_furnitureName) && RentalSystem.furnitures.get(i).getCustomer() != null){
                        if(RentalSystem.furnitures.get(i).getCustomer().getAllField().equals(inv_tableAllField)){
                            RentalSystem.furnitures.get(i).freeCustomer();
                            RentalSystem.customers.get(customerIndex).reduceCustomerFurniture(RentalSystem.furnitures.get(i));
                            log.add(getTime() + "   " + nowLoginUserName + " Return " + RentalSystem.customers.get(customerIndex).getAllField() + " " + inv_furnitureName);
                            break;
                        }
                    }
                }

                invOpenTableView();
                if(keepRow < invOpen_invTable.getRowCount())
                    invOpen_invTable.setRowSelectionInterval(keepRow, keepRow);
                }
            }
        }
        
    }

    class NewCustomerListener implements ActionListener{

        private boolean check(int index, String name, int age, String gender, String contrateDate){
            String allField = name + "," + age + "," + gender + "," + contrateDate;
            if(RentalSystem.customers.get(index).getAllField().equals(allField)){
                return true;
            }else{
                return false;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = cusNew_nameField.getText();
            CardChangeListener cardChangeListener = new CardChangeListener();
            
            if(!name.isEmpty()){
                int age = (int)cusNew_ageComboBox.getSelectedItem();
                String gender = (String)cusNew_genderComboBox.getSelectedItem();
                String contrateDate = cusNew_yearComboBox.getSelectedItem() + "/" + cusNew_monthComboBox.getSelectedItem() + "/" + cusNew_dayComboBox.getSelectedItem();

                int customersSize = RentalSystem.customers.size();
                if(name != ""){
                    for(int i = 0; i < customersSize; i++){
                        if(check(i, name, age, gender, contrateDate)){
                            JOptionPane.showMessageDialog(null, "this customer is already in table", "Error" ,JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        if(RentalSystem.customers.size() == i + 1){
                            RentalSystem.customers.add(new Customer(name, age, gender, contrateDate));
                            RentalSystem.users.add(new User(name, "password"));
                            log.add(getTime() + "   " + nowLoginUserName + " NewCustomer " +  RentalSystem.customers.get(RentalSystem.customers.size() - 1).getAllField());
                            cusTableView();
                            worTableView();
                            cardChangeListener.actionPerformed(e);
                            //nowCardPanel = "customers";
                            //layout.show(cardPanel,"customers");
                        }
                    }

                    if(RentalSystem.customers.size() == 0){
                        RentalSystem.customers.add(new Customer(name, age, gender, contrateDate));
                        RentalSystem.users.add(new User(name, "password"));
                        log.add(getTime() + "   " + nowLoginUserName + " NewCustomer " +  RentalSystem.customers.get(RentalSystem.customers.size() - 1).getAllField());
                        cusTableView();
                        worTableView();
                        cardChangeListener.actionPerformed(e);
                        //nowCardPanel = "customers";
                        //layout.show(cardPanel,"customers");
                    }
                }
            }
        } 
    }

    class RemoveCustomerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(cusTable.getSelectedRow() >= 0){
                int rowIndex = cusTable.convertRowIndexToModel(cusTable.getSelectedRow());
                String name = (String)cusTableModel.getValueAt(rowIndex, 0);
                String cus_allField = (String)cusTableModel.getValueAt(rowIndex, 0) + "," + (int) cusTableModel.getValueAt(rowIndex, 1) + "," + (String)cusTableModel.getValueAt(rowIndex, 2) + "," + (String)cusTableModel.getValueAt(rowIndex,3);
                for(int i = 0; i < RentalSystem.customers.size(); i++){
                    if(cus_allField.equals(RentalSystem.customers.get(i).getAllField())){
                        if(RentalSystem.customers.get(i).getCustomerFurnitures().size() == 0){
                            log.add(getTime() + "   " + nowLoginUserName + " RemoveCustomer " +  RentalSystem.customers.get(i).getAllField());
                            RentalSystem.customers.remove(i);
                        }
                        break;
                    }
                }
                for(int i = 0; i < RentalSystem.users.size(); i++){
                    if(RentalSystem.users.get(i).getName().equals(name)){
                        RentalSystem.users.remove(i);
                        break;
                    }
                }

                cusTableView();
                worTableView();
            }
        }
        
    }

    class YearComboBoxListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int year = (int) cusNew_yearComboBoxModel.getSelectedItem();
            int month = (int) cusNew_monthComboBoxModel.getSelectedItem();
            int day = (int) cusNew_dayComboBoxModel.getSelectedItem();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, Calendar.FEBRUARY, 1);
            int numberOfdays = calendar.getActualMaximum(Calendar.DATE);
            if (numberOfdays == 28 && month == 2) {
                
                cusNew_dayComboBox.removeAllItems();
                for(int i = 1; i <= 28; i++){
                    cusNew_dayComboBox.addItem(i);
                }
                if(day >= 29){
                    cusNew_dayComboBoxModel.setSelectedItem(28);
                }else{
                    cusNew_dayComboBoxModel.setSelectedItem(day);
                }
		    }else if (month == 2){
                cusNew_dayComboBox.removeAllItems();
                for(int i = 1; i <= 29; i++){
                    cusNew_dayComboBox.addItem(i);
                }
                if(day >= 30){
                    cusNew_dayComboBoxModel.setSelectedItem(29);
                }else{
                    cusNew_dayComboBoxModel.setSelectedItem(day);
                }
            }
        }
        
    }

    class MonthComboBoxListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int year = (int) cusNew_yearComboBoxModel.getSelectedItem();
            int month = (int) cusNew_monthComboBoxModel.getSelectedItem();
            int day = (int) cusNew_dayComboBoxModel.getSelectedItem();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, Calendar.FEBRUARY, 1);
            int numberOfdays = calendar.getActualMaximum(Calendar.DATE);
            if (numberOfdays == 28 && month == 2) {
                
                cusNew_dayComboBox.removeAllItems();
                for(int i = 1; i <= 28; i++){
                    cusNew_dayComboBox.addItem(i);
                }
                if(day >= 29){
                    cusNew_dayComboBoxModel.setSelectedItem(28);
                }else{
                    cusNew_dayComboBoxModel.setSelectedItem(day);
                }
		    }else if (month == 2){
                cusNew_dayComboBox.removeAllItems();
                for(int i = 1; i <= 29; i++){
                    cusNew_dayComboBox.addItem(i);
                }
                if(day >= 30){
                    cusNew_dayComboBoxModel.setSelectedItem(29);
                }else{
                    cusNew_dayComboBoxModel.setSelectedItem(day);
                }
            }else if(month == 4 || month == 6 || month == 9 || month == 11){
                cusNew_dayComboBox.removeAllItems();
                for(int i = 1; i <= 30; i++){
                    cusNew_dayComboBox.addItem(i);
                }
                if(day >= 31){
                    cusNew_dayComboBoxModel.setSelectedItem(30);
                }else{
                    cusNew_dayComboBoxModel.setSelectedItem(day);
                }
            }else{
                cusNew_dayComboBox.removeAllItems();
                for(int i = 1; i <= 31; i++){
                    cusNew_dayComboBox.addItem(i);
                }
                cusNew_dayComboBoxModel.setSelectedItem(day);
            }
        }
        
    }

    class CustomerOpenListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {   
            cusOpenTableView();
            northLabel.setText("HOME > CUSTOMERS > OPEN " + RentalSystem.customers.get(cusOpen_customerIndex).getName());
        }
    }

    class CustomerTableSelectedListener implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(cusTable.getSelectedRow() >= 0){
                cus_openButton.setEnabled(true);
                int rowIndex = cusTable.convertRowIndexToModel(cusTable.getSelectedRow());
                cus_tableAllField = (String)cusTableModel.getValueAt(rowIndex, 0) + "," + (int) cusTableModel.getValueAt(rowIndex, 1) + "," + (String)cusTableModel.getValueAt(rowIndex, 2) + "," + (String)cusTableModel.getValueAt(rowIndex,3);
            }
        }
    }

    class CustomerRendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(cusOpen_invTable.getSelectedRow() >= 0){
                int keepRow = cusOpen_invTable.getSelectedRow();
                String name = (String) cusOpen_invTableModel.getValueAt(cusOpen_invTable.getRowSorter().convertRowIndexToModel(cusOpen_invTable.getSelectedRow()), 0);
                for(int i = 0; i < RentalSystem.furnitures.size(); i++){
                    if(RentalSystem.furnitures.get(i).getName().equals(name) && RentalSystem.furnitures.get(i).getCustomer() == null){
                        RentalSystem.furnitures.get(i).setCustomer(RentalSystem.customers.get(cusOpen_customerIndex));
                        RentalSystem.customers.get(cusOpen_customerIndex).addCustomerFurniture(RentalSystem.furnitures.get(i));
                        log.add(getTime() + "   " + nowLoginUserName + " Rend " +  RentalSystem.furnitures.get(i).getName() + " " + RentalSystem.customers.get(cusOpen_customerIndex).getAllField());
                        break;
                    }
                }

                cusOpenTableView();
                if(keepRow < cusOpen_invTable.getRowCount())
                    cusOpen_invTable.setRowSelectionInterval(keepRow, keepRow);
            }

            
        }
    }

    class CustomerReturnListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int keepRow = cusOpen_cusTable.getSelectedRow();
            if(cusOpen_cusTable.getSelectedRow() >= 0){
                int rowIndex = cusOpen_cusTable.convertRowIndexToModel(cusOpen_cusTable.getSelectedRow());
                String name = (String) cusOpen_cusTableModel.getValueAt(rowIndex, 0);
                for(int i = 0; i < RentalSystem.furnitures.size(); i++){
                    if(RentalSystem.furnitures.get(i).getName().equals(name) && RentalSystem.furnitures.get(i).getCustomer() != null){
                        if(RentalSystem.furnitures.get(i).getCustomer().getAllField().equals(RentalSystem.customers.get(cusOpen_customerIndex).getAllField())){
                            RentalSystem.furnitures.get(i).freeCustomer();
                            RentalSystem.customers.get(cusOpen_customerIndex).reduceCustomerFurniture(RentalSystem.furnitures.get(i));
                            log.add(getTime() + "   " + nowLoginUserName + " Return " + RentalSystem.customers.get(cusOpen_customerIndex).getAllField() + " " + RentalSystem.furnitures.get(i).getName());
                            break;
                        }
                    }
                }
                cusOpenTableView();
                if(keepRow < cusOpen_cusTable.getRowCount())
                    cusOpen_cusTable.setRowSelectionInterval(keepRow, keepRow);
            }

            
        }
        
    }

    class NewWorkerListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = worNew_nameField.getText();
            CardChangeListener cardChangeListener = new CardChangeListener();
            if(!userName.isEmpty()){
                for(int i = 0; i < RentalSystem.users.size(); i++){
                    User worker = RentalSystem.users.get(i);
                    if(worker.getName().equals(userName)){
                        JOptionPane.showMessageDialog(null, "this admin is already in table", "Error" ,JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    if(i + 1 == RentalSystem.users.size()){
                        RentalSystem.users.add(new User(userName, "password", true));
                        worTableView();
                        log.add(getTime() + "   " + nowLoginUserName + " NewWorker " + userName);
                        cardChangeListener.actionPerformed(e);
                        //nowCardPanel = "users";
                        //layout.show(cardPanel, "users");
                        break;
                    }
                }
            }
        }
    }

    class RemoveWorkerListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int rowIndex = worTable.convertRowIndexToModel(worTable.getSelectedRow());
            String userName = (String) worTable.getValueAt(rowIndex, 0);
            String rank = (String) worTable.getValueAt(rowIndex, 1);
            if(rowIndex >= 0 && !userName.equals("admin") && rank.equals("Admin")){
                for(int i = 0; i < RentalSystem.users.size(); i++){
                    User worker = RentalSystem.users.get(i);
                    if(worker.getName().equals(userName)){
                        log.add(getTime() + "   " + nowLoginUserName + " RemoveWorker " + userName);
                        RentalSystem.users.remove(i);
                        worTableView();
                        break;
                    }
                }
            }
            
        }
        
    }

    class ChangePasswordListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String currentPassword = new String(cpass_currentPasswordField.getPassword());
            String newPassword = new String(cpass_newPasswordField.getPassword());
            String confirmNewPassword = new String(cpass_confirmNewPasswordField.getPassword());
            if(!currentPassword.equals(nowLoginPassword)){
                JOptionPane.showMessageDialog(null, "Wrong Current Login Password", "Error" ,JOptionPane.ERROR_MESSAGE);
            }
            if(!newPassword.equals(confirmNewPassword)){
                JOptionPane.showMessageDialog(null, "New Password is not the same ConfirmNewPassword", "Error" ,JOptionPane.ERROR_MESSAGE);
            }
            if(currentPassword.equals(nowLoginPassword) && newPassword.equals(confirmNewPassword) && !newPassword.isEmpty()){
                for(int i = 0; i < RentalSystem.users.size(); i++){
                    User worker = RentalSystem.users.get(i);
                    if(worker.getName().equals(nowLoginUserName) && worker.getPassword().equals(nowLoginPassword)){
                        worker.setPassword(newPassword);
                        nowLoginPassword = newPassword;
                        cpass_currentPasswordField.setText("");
                        cpass_newPasswordField.setText("");
                        cpass_confirmNewPasswordField.setText("");
                        layout.show(cardPanel, "home");
                        leftPanel.setVisible(false);
                        log.add(getTime() + "   " + nowLoginUserName + " ChangePassword");
                        break;
                    }
                }   
            }
        }
    }

    class BackListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(nowCardPanel){
                case "inventory":
                case "customers":
                case "users":
                    layout.show(cardPanel, "home");
                    northLabel.setText("HOME");
                    leftPanel.setVisible(false);
                    break;
                case "inv_newPanel":
                    layout.show(cardPanel, "inventory");
                    northLabel.setText("HOME > INVENTORY");
                    nowCardPanel = "inventory";
                    break;
                case "inv_openPanel":
                    layout.show(cardPanel, "inventory");
                    northLabel.setText("HOME > INVENTORY");
                    nowCardPanel = "inventory";
                    break;
                case "cus_newPanel":
                    layout.show(cardPanel, "customers");
                    northLabel.setText("HOME > CUSTOMERS");
                    nowCardPanel = "customers";
                    break;
                case "cus_openPanel":
                    layout.show(cardPanel, "customers");
                    northLabel.setText("HOME > CUSTOMERS");
                    nowCardPanel = "customers";
                    break;
                case "wor_newPanel":
                    layout.show(cardPanel, "users");
                    northLabel.setText("HOME > USERS");
                    nowCardPanel = "users";
                    break;
                default:
                    layout.show(cardPanel, "home");
                    northLabel.setText("HOME");
                    leftPanel.setVisible(false);
            } 
        }
        
    }

    public static void Gui(){
        GUI frame = new GUI("Furnitures Rental System");
        frame.setMinimumSize(new Dimension(800,600));
        frame.setBounds(250,50,800,600);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    GUI(String title){
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardChangeListener cardChangeListener = new CardChangeListener();
        LoginListener loginListener = new LoginListener();
        NewFurnitureListener newFurnitureListener = new NewFurnitureListener();
        AddFurnitureListener addFurnitureListener = new AddFurnitureListener();
        RemoveFurnitureListener removeFurnitureListener = new RemoveFurnitureListener();
        YearComboBoxListener yearComboBoxListener = new YearComboBoxListener();
        MonthComboBoxListener monthComboBoxListener = new MonthComboBoxListener();
        NewCustomerListener newCustomerListener = new NewCustomerListener();
        RemoveCustomerListener removeCustomerListener = new RemoveCustomerListener();
        InventoryTableSelectedListener inventoryTableSelectedListener = new InventoryTableSelectedListener();
        InventoryOpenListener inventoryOpenListener = new InventoryOpenListener();
        InventoryRendListener inventoryRendListener = new InventoryRendListener();
        InventoryReturnListener inventoryReturnListener = new InventoryReturnListener();
        CustomerTableSelectedListener customerTableSelectedListener = new CustomerTableSelectedListener();
        CustomerOpenListener customerOpenListener = new CustomerOpenListener();
        CustomerRendListener customerRendListener = new CustomerRendListener();
        CustomerReturnListener customerReturnListener = new CustomerReturnListener();
        NewWorkerListener newWorkerListener = new NewWorkerListener();
        RemoveWorkerListener removeWorkerListener = new RemoveWorkerListener();
        ChangePasswordListener changePasswordListener = new ChangePasswordListener();
        BackListener backListener = new BackListener();

        /* LOGIN PANEL */
        JPanel loginPanel = new JPanel();
        JPanel login_fieldPanel = new JPanel();

        login_userNameField = new JTextField(15);
        login_passwordField = new JPasswordField(15);

        login_fieldPanel.setLayout(new BoxLayout(login_fieldPanel, BoxLayout.Y_AXIS));
        login_fieldPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        JButton loginButton = new JButton("Login");
        loginButton.setMaximumSize(new Dimension(130, 50));
        loginButton.addActionListener(loginListener);

        login_fieldPanel.add(Box.createRigidArea(new Dimension(1,100)));
        login_fieldPanel.add(new JLabel("UserName"));
        login_fieldPanel.add(Box.createRigidArea(new Dimension(1,3)));
        login_fieldPanel.add(login_userNameField);
        login_fieldPanel.add(Box.createRigidArea(new Dimension(1,10)));
        login_fieldPanel.add(new JLabel("Password"));
        login_fieldPanel.add(Box.createRigidArea(new Dimension(1,3)));
        login_fieldPanel.add(login_passwordField);
        login_fieldPanel.add(Box.createRigidArea(new Dimension(1,10)));
        login_fieldPanel.add(loginButton);
        
        loginPanel.add(login_fieldPanel);

        /* NORTH PANEL */

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northLabel = new JLabel("LOGIN");
        northLabel.setPreferredSize(new Dimension(100,60));
        northLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        northPanel.add(Box.createRigidArea(new Dimension(30,1)));
        northPanel.add(northLabel);

        /* LEFT PANEL */

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        JButton backButton = new JButton("BACK");
        backButton.addActionListener(backListener);
        leftPanel.add(backButton);

        leftPanel.setVisible(false);

        /* INVENTORY [NEW] PANEL */
        JPanel inv_newPanel = new JPanel();
        inv_newPanel.setLayout(new BoxLayout(inv_newPanel, BoxLayout.Y_AXIS));
        JPanel invNew_labePanel = new JPanel();
        invNew_labePanel.setLayout(new BoxLayout(invNew_labePanel, BoxLayout.X_AXIS));
        JPanel invNew_fieldPanel = new JPanel();
        invNew_fieldPanel.setLayout(new BoxLayout(invNew_fieldPanel, BoxLayout.X_AXIS));
        JPanel invNew_buttonPanel = new JPanel();
        invNew_buttonPanel.setLayout(new BoxLayout(invNew_buttonPanel, BoxLayout.X_AXIS));
        JButton invNew_confirmButton = new JButton("Confirm");
        invNew_confirmButton.addActionListener(newFurnitureListener);

        invNew_labePanel.add(Box.createRigidArea(new Dimension(90,1)));
        invNew_labePanel.add(new JLabel("Name"));
        invNew_labePanel.add(Box.createRigidArea(new Dimension(220,1)));
        invNew_labePanel.add(new JLabel("Quantity"));
        invNew_labePanel.add(Box.createRigidArea(new Dimension(180,1)));

        for(int i = 1; i <= 100; i++){
            invNew_quantityComboBox.addItem(i);
        }

        invNew_fieldPanel.add(Box.createRigidArea(new Dimension(130,1)));
        invNew_fieldPanel.add(invNew_nameField);
        invNew_fieldPanel.add(Box.createRigidArea(new Dimension(50,1)));
        invNew_fieldPanel.add(invNew_quantityComboBox);
        invNew_fieldPanel.add(Box.createRigidArea(new Dimension(230,1)));

        invNew_buttonPanel.add(invNew_confirmButton);

        inv_newPanel.add(Box.createRigidArea(new Dimension(1,50)));
        inv_newPanel.add(invNew_labePanel);
        inv_newPanel.add(Box.createRigidArea(new Dimension(1,10)));
        inv_newPanel.add(invNew_fieldPanel);
        inv_newPanel.add(Box.createRigidArea(new Dimension(1,10)));
        inv_newPanel.add(invNew_buttonPanel);
        inv_newPanel.add(Box.createRigidArea(new Dimension(1,600)));

        /* INVENTORY [OPEN] PANEL */

        JPanel inv_openPanel = new JPanel();

        inv_openPanel.setLayout(new BoxLayout(inv_openPanel, BoxLayout.Y_AXIS));

        JPanel invOpen_labelPanel = new JPanel();
        invOpen_labelPanel.add(new JLabel("Inventory"));
        invOpen_labelPanel.add(Box.createRigidArea(new Dimension(180,1)));
        invOpen_labelPanel.add(new JLabel("Customer"));
        invOpen_labelPanel.add(Box.createRigidArea(new Dimension(160,1)));

        JPanel invOpen_tablePanel = new JPanel();
        invOpen_tablePanel.setLayout(new BoxLayout(invOpen_tablePanel, BoxLayout.X_AXIS));
        String[] invOpen_leftColumns = {"Name","Age","Gender","Contrate Date","Quantity"};
        invOpen_invTableModel = new DefaultTableModel(invOpen_leftColumns, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return false;                                             //table edit false
            }
        };
        String[] invOpen_rightColumns = {"Name","Age","Gender","Contrate Date"};
        invOpen_cusTableModel = new DefaultTableModel(invOpen_rightColumns, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return false;                                             //table edit false
            }
        };

        invOpen_invTable = new JTable(invOpen_invTableModel);
        invOpen_cusTable = new JTable(invOpen_cusTableModel);
        invOpen_invTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invOpen_cusTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableColumnModel invOpen_invColumnModel = (DefaultTableColumnModel) invOpen_invTable.getColumnModel();
        DefaultTableColumnModel invOpen_cusColumnModel = (DefaultTableColumnModel) invOpen_cusTable.getColumnModel();

        invOpen_invColumnModel.getColumn(0).setPreferredWidth(100);
        invOpen_invColumnModel.getColumn(1).setPreferredWidth(50);
        invOpen_invColumnModel.getColumn(2).setPreferredWidth(130);
        invOpen_invColumnModel.getColumn(3).setPreferredWidth(100);
        invOpen_invColumnModel.getColumn(4).setPreferredWidth(100);
        invOpen_invTable.setAutoCreateRowSorter(true);
        invOpen_invTable.setRowSorter(new TableRowSorter<>(invOpen_invTableModel));

        invOpen_cusColumnModel.getColumn(0).setPreferredWidth(150);
        invOpen_cusColumnModel.getColumn(1).setPreferredWidth(55);
        invOpen_cusColumnModel.getColumn(2).setPreferredWidth(55);
        invOpen_cusColumnModel.getColumn(3).setPreferredWidth(100);
        invOpen_cusTable.setAutoCreateRowSorter(true);
        invOpen_cusTable.setRowSorter(new TableRowSorter<>(invOpen_cusTableModel));

        JScrollPane invOpen_invScrollPane = new JScrollPane(invOpen_invTable);
		invOpen_invScrollPane.createVerticalScrollBar();
		invOpen_invScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollPane invOpen_cusScrollPane = new JScrollPane(invOpen_cusTable);
        invOpen_cusScrollPane.setPreferredSize(new Dimension(400,700));
		invOpen_cusScrollPane.createVerticalScrollBar();
		invOpen_cusScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        invOpen_tablePanel.add(invOpen_invScrollPane);
        invOpen_tablePanel.add(Box.createRigidArea(new Dimension(30,1)));
        invOpen_tablePanel.add(invOpen_cusScrollPane);
        invOpen_tablePanel.add(Box.createRigidArea(new Dimension(100,1)));

        JPanel invOpen_ButtonPanel = new JPanel();
        invOpen_ButtonPanel.setLayout(new BoxLayout(invOpen_ButtonPanel, BoxLayout.X_AXIS));
        JButton invOpen_returnButton = new JButton("Return");
        invOpen_returnButton.addActionListener(inventoryReturnListener);
        JButton invOpen_rendButton = new JButton("Rend");
        invOpen_rendButton.addActionListener(inventoryRendListener);

        invOpen_ButtonPanel.add(Box.createRigidArea(new Dimension(70,1)));
        invOpen_ButtonPanel.add(invOpen_returnButton);
        invOpen_ButtonPanel.add(Box.createRigidArea(new Dimension(233,1)));
        invOpen_ButtonPanel.add(invOpen_rendButton);
        

        inv_openPanel.add(Box.createRigidArea(new Dimension(1,70)));
        inv_openPanel.add(invOpen_labelPanel);
        inv_openPanel.add(Box.createRigidArea(new Dimension(1,10)));
        inv_openPanel.add(invOpen_tablePanel);
        inv_openPanel.add(Box.createRigidArea(new Dimension(1,10)));
        inv_openPanel.add(invOpen_ButtonPanel);
        inv_openPanel.add(Box.createRigidArea(new Dimension(1,30)));

        
        /* CUSTOMER [NEW] PANEL */
        JPanel cus_newPanel = new JPanel();
        cus_newPanel.setLayout(new BoxLayout(cus_newPanel, BoxLayout.Y_AXIS));
        JPanel cusNew_labelPanel = new JPanel();
        cusNew_labelPanel.setLayout(new BoxLayout(cusNew_labelPanel, BoxLayout.X_AXIS));
        JPanel cusNew_fieldPanel = new JPanel();
        cusNew_fieldPanel.setLayout(new BoxLayout(cusNew_fieldPanel, BoxLayout.X_AXIS));
        JPanel cusNew_buttonPanel = new JPanel();
        cusNew_buttonPanel.setLayout(new BoxLayout(cusNew_buttonPanel, BoxLayout.X_AXIS));
        JButton cusNew_confirmButton = new JButton("Confirm");
        cusNew_confirmButton.setActionCommand("customers");
        cusNew_confirmButton.addActionListener(newCustomerListener);

        cusNew_labelPanel.add(Box.createRigidArea(new Dimension(30,1)));
        cusNew_labelPanel.add(new JLabel("Name"));
        cusNew_labelPanel.add(Box.createRigidArea(new Dimension(185,1)));
        cusNew_labelPanel.add(new JLabel("age"));
        cusNew_labelPanel.add(Box.createRigidArea(new Dimension(45,1)));
        cusNew_labelPanel.add(new JLabel("gender"));
        cusNew_labelPanel.add(Box.createRigidArea(new Dimension(50,1)));
        cusNew_labelPanel.add(new JLabel("Contract Date"));
        cusNew_labelPanel.add(Box.createRigidArea(new Dimension(180,1)));

        for(int i = 18; i <= 150; i++){
            cusNew_ageComboBox.addItem(i);
        }
        cusNew_genderComboBox.addItem("man");
        cusNew_genderComboBox.addItem("woman");
        for(int i = 1900; i <= 2100; i++){
            cusNew_yearComboBox.addItem(i);
        }
        cusNew_yearComboBox.addActionListener(yearComboBoxListener);
        for(int i = 1; i <= 12; i++){
            cusNew_monthComboBox.addItem(i);
        }
        cusNew_monthComboBox.addActionListener(monthComboBoxListener);
        for(int i = 1; i <= 31; i++){
            cusNew_dayComboBox.addItem(i);
        }

        cusNew_fieldPanel.add(Box.createRigidArea(new Dimension(30,1)));
        cusNew_fieldPanel.add(cusNew_nameField);
        cusNew_fieldPanel.add(Box.createRigidArea(new Dimension(20,1)));
        cusNew_fieldPanel.add(cusNew_ageComboBox);
        cusNew_fieldPanel.add(Box.createRigidArea(new Dimension(20,1)));
        cusNew_fieldPanel.add(cusNew_genderComboBox);
        cusNew_fieldPanel.add(Box.createRigidArea(new Dimension(20,1)));
        cusNew_fieldPanel.add(cusNew_yearComboBox);
        cusNew_fieldPanel.add(new JLabel(" / "));
        cusNew_fieldPanel.add(cusNew_monthComboBox);
        cusNew_fieldPanel.add(new JLabel(" / "));
        cusNew_fieldPanel.add(cusNew_dayComboBox);
        cusNew_fieldPanel.add(Box.createRigidArea(new Dimension(100,1)));

        cusNew_buttonPanel.add(cusNew_confirmButton);

        cus_newPanel.add(Box.createRigidArea(new Dimension(1,50)));
        cus_newPanel.add(cusNew_labelPanel);
        cus_newPanel.add(Box.createRigidArea(new Dimension(1,10)));
        cus_newPanel.add(cusNew_fieldPanel);
        cus_newPanel.add(Box.createRigidArea(new Dimension(1,10)));
        cus_newPanel.add(cusNew_buttonPanel);
        cus_newPanel.add(Box.createRigidArea(new Dimension(1,600)));


        /* CUSTOMER [OPEN] PANEL */

        JPanel cus_openPanel = new JPanel();
        cus_openPanel.setLayout(new BoxLayout(cus_openPanel, BoxLayout.Y_AXIS));

        JPanel cusOpen_labelPanel = new JPanel();
        cusOpen_labelPanel.add(new JLabel("Customer Rental Furniture"));
        cusOpen_labelPanel.add(Box.createRigidArea(new Dimension(180,1)));
        cusOpen_labelPanel.add(new JLabel("Inventory"));
        cusOpen_labelPanel.add(Box.createRigidArea(new Dimension(160,1)));

        JPanel cusOpen_tablePanel = new JPanel();
        cusOpen_tablePanel.setLayout(new BoxLayout(cusOpen_tablePanel, BoxLayout.X_AXIS));
        String[] cusOpen_columns = {"Name","Quantity"};
        cusOpen_cusTableModel = new DefaultTableModel(cusOpen_columns, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return false;                                             //table edit false
            }
        };

        cusOpen_invTableModel = new DefaultTableModel(cusOpen_columns, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return false;                                             //table edit false
            }
        };

        cusOpen_cusTable = new JTable(cusOpen_cusTableModel);
        cusOpen_invTable = new JTable(cusOpen_invTableModel);
        cusOpen_cusTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cusOpen_invTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableColumnModel cusOpen_cusColumnModel = (DefaultTableColumnModel) cusOpen_cusTable.getColumnModel();
        DefaultTableColumnModel cusOpen_invColumnModel = (DefaultTableColumnModel) cusOpen_invTable.getColumnModel();

        cusOpen_cusColumnModel.getColumn(0).setPreferredWidth(200);
        cusOpen_cusColumnModel.getColumn(1).setPreferredWidth(55);
        cusOpen_cusTable.setAutoCreateRowSorter(true);

        cusOpen_invColumnModel.getColumn(0).setPreferredWidth(200);
        cusOpen_invColumnModel.getColumn(1).setPreferredWidth(55);
        cusOpen_invTable.setAutoCreateRowSorter(true);

        JScrollPane cusOpen_cusScrollPane = new JScrollPane(cusOpen_cusTable);
		cusOpen_cusScrollPane.createVerticalScrollBar();
		cusOpen_cusScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollPane cusOpen_invScrollPane = new JScrollPane(cusOpen_invTable);
		cusOpen_invScrollPane.createVerticalScrollBar();
		cusOpen_invScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        cusOpen_tablePanel.add(cusOpen_cusScrollPane);
        cusOpen_tablePanel.add(Box.createRigidArea(new Dimension(30,1)));
        cusOpen_tablePanel.add(cusOpen_invScrollPane);
        cusOpen_tablePanel.add(Box.createRigidArea(new Dimension(100,1)));

        JPanel cusOpen_ButtonPanel = new JPanel();
        cusOpen_ButtonPanel.setLayout(new BoxLayout(cusOpen_ButtonPanel, BoxLayout.X_AXIS));
        JButton cusOpen_returnButton = new JButton("Return");
        cusOpen_returnButton.addActionListener(customerReturnListener);
        JButton cusOpen_rendButton = new JButton("Rend");
        cusOpen_rendButton.addActionListener(customerRendListener);

        cusOpen_ButtonPanel.add(Box.createRigidArea(new Dimension(70,1)));
        cusOpen_ButtonPanel.add(cusOpen_returnButton);
        cusOpen_ButtonPanel.add(Box.createRigidArea(new Dimension(233,1)));
        cusOpen_ButtonPanel.add(cusOpen_rendButton);
        

        cus_openPanel.add(Box.createRigidArea(new Dimension(1,70)));
        cus_openPanel.add(cusOpen_labelPanel);
        cus_openPanel.add(Box.createRigidArea(new Dimension(1,10)));
        cus_openPanel.add(cusOpen_tablePanel);
        cus_openPanel.add(Box.createRigidArea(new Dimension(1,10)));
        cus_openPanel.add(cusOpen_ButtonPanel);
        cus_openPanel.add(Box.createRigidArea(new Dimension(1,30)));

        /* WORKER [NEW] PANEL */

        JPanel wor_newPanel = new JPanel();
        wor_newPanel.setLayout(new BoxLayout(wor_newPanel, BoxLayout.Y_AXIS));
        JPanel worNew_labelPanel = new JPanel();
        worNew_labelPanel.setLayout(new BoxLayout(worNew_labelPanel, BoxLayout.X_AXIS));
        JPanel worNew_fieldPanel = new JPanel();
        worNew_fieldPanel.setLayout(new BoxLayout(worNew_fieldPanel, BoxLayout.X_AXIS));
        JPanel worNew_buttonPanel = new JPanel();
        worNew_buttonPanel.setLayout(new BoxLayout(worNew_buttonPanel, BoxLayout.X_AXIS));
        JButton worNew_confirmButton = new JButton("Confirm");
        worNew_confirmButton.setActionCommand("users");
        worNew_confirmButton.addActionListener(newWorkerListener);

        worNew_labelPanel.add(Box.createRigidArea(new Dimension(30,1)));
        worNew_labelPanel.add(new JLabel("Name"));
        worNew_labelPanel.add(Box.createRigidArea(new Dimension(365,1)));

        worNew_fieldPanel.add(Box.createRigidArea(new Dimension(120,1)));
        worNew_fieldPanel.add(worNew_nameField);
        worNew_fieldPanel.add(Box.createRigidArea(new Dimension(270,1)));

        worNew_buttonPanel.add(worNew_confirmButton);

        wor_newPanel.add(Box.createRigidArea(new Dimension(1,50)));
        wor_newPanel.add(worNew_labelPanel);
        wor_newPanel.add(Box.createRigidArea(new Dimension(1,10)));
        wor_newPanel.add(worNew_fieldPanel);
        wor_newPanel.add(Box.createRigidArea(new Dimension(1,10)));
        wor_newPanel.add(worNew_buttonPanel);
        wor_newPanel.add(Box.createRigidArea(new Dimension(1,600)));

        /* HOME PANEL */
        JPanel home = new JPanel();
        JPanel homeTab1 = new JPanel();
        JPanel homeTab2 = new JPanel();
        JPanel homeTab3 = new JPanel();
        home.setLayout(new GridLayout(0,3));
        
        home.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        
        homeTab1.setLayout(new BoxLayout(homeTab1, BoxLayout.Y_AXIS));
        homeTab1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        homeTab2.setLayout(new BoxLayout(homeTab2, BoxLayout.Y_AXIS));
        homeTab2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        homeTab3.setLayout(new BoxLayout(homeTab3, BoxLayout.Y_AXIS));
        homeTab3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton inventoryBtn = new JButton("Inventory");
        inventoryBtn.setMaximumSize(new Dimension(400,50));
        inventoryBtn.setActionCommand("inventory");
        inventoryBtn.addActionListener(cardChangeListener);
        
        JButton customersBtn = new JButton("Customers");
        customersBtn.addActionListener(cardChangeListener);
        customersBtn.setActionCommand("customers");
        customersBtn.setMaximumSize(new Dimension(400,50));

        usersBtn = new JButton("Users");
        usersBtn.addActionListener(cardChangeListener);
        usersBtn.setActionCommand("users");
        usersBtn.setMaximumSize(new Dimension(400,50));

        JButton changePasswordBtn = new JButton("Change Password");
        changePasswordBtn.addActionListener(cardChangeListener);
        changePasswordBtn.setActionCommand("changePassword");
        changePasswordBtn.setMaximumSize(new Dimension(400,50));

        homeTab1.add(inventoryBtn);
        homeTab2.add(customersBtn);
        homeTab3.add(usersBtn);
        homeTab3.add(Box.createRigidArea(new Dimension(1,15)));
        homeTab3.add(changePasswordBtn);

        home.add(homeTab1);
        home.add(homeTab2);
        home.add(homeTab3);
        

        /* INVENTORY PANEL */
        JPanel inventory = new JPanel();
        inventory.setLayout(new BoxLayout(inventory, BoxLayout.Y_AXIS));
        inventory.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 100));


        JPanel inv_addrmPanel = new JPanel();
        inv_addrmPanel.setLayout(new BoxLayout(inv_addrmPanel, BoxLayout.X_AXIS));
        
        inv_newButton = new JButton("new");
        inv_newButton.setMaximumSize(new Dimension(80,30));
        inv_newButton.setActionCommand("inv_newPanel");
        inv_newButton.addActionListener(cardChangeListener);

        inv_addButton = new JButton("add");
        inv_addButton.setMaximumSize(new Dimension(80,30));
        inv_addButton.addActionListener(addFurnitureListener);

        inv_removeButton = new JButton("remove");
        inv_removeButton.addActionListener(removeFurnitureListener);
        inv_removeButton.setMaximumSize(new Dimension(80,30));
        inv_addrmPanel.add(inv_newButton);
        inv_addrmPanel.add(Box.createRigidArea(new Dimension(30,1)));
        inv_addrmPanel.add(inv_addButton);
        inv_addrmPanel.add(Box.createRigidArea(new Dimension(10,1)));
        inv_addrmPanel.add(inv_addQuantityComboBox);
        inv_addrmPanel.add(Box.createRigidArea(new Dimension(220,1)));
        inv_addrmPanel.add(inv_removeButton);
        inv_addrmPanel.add(Box.createRigidArea(new Dimension(10,1)));
        inv_addrmPanel.add(inv_removeQuantityComboBox);

        String[] inv_columns = {"Name","Quantity"};
        invTableModel = new DefaultTableModel(inv_columns, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return false;                                             //table edit false
            }
        };

        invTable = new JTable(invTableModel);
        invTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableColumnModel inv_columnModel = (DefaultTableColumnModel) invTable.getColumnModel();
        
        invTableView();

		inv_columnModel.getColumn(0).setPreferredWidth(493);
        inv_columnModel.getColumn(1).setPreferredWidth(55);
        invTable.setAutoCreateRowSorter(true);

        invTable.getSelectionModel().addListSelectionListener(inventoryTableSelectedListener);

        JScrollPane inv_scrollPane = new JScrollPane(invTable);
		inv_scrollPane.createVerticalScrollBar();
		inv_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel inv_lowPanel = new JPanel();
        inv_lowPanel.setLayout(new BoxLayout(inv_lowPanel, BoxLayout.X_AXIS));

        inv_openButton = new JButton("open");
        inv_openButton.setEnabled(false);
        inv_openButton.setMaximumSize(new Dimension(80,30));
        inv_openButton.setActionCommand("inv_openPanel");
        inv_openButton.addActionListener(cardChangeListener);
        inv_openButton.addActionListener(inventoryOpenListener);

        inv_lowPanel.add(Box.createRigidArea(new Dimension(487,1)));
        inv_lowPanel.add(inv_openButton);

        inventory.add(Box.createRigidArea(new Dimension(1,70)));
        inventory.add(inv_addrmPanel);
        inventory.add(Box.createRigidArea(new Dimension(1,10)));
        inventory.add(inv_scrollPane);
        inventory.add(Box.createRigidArea(new Dimension(1,10)));
        inventory.add(inv_lowPanel);


        /* CUSTOMERS PANEL */
        JPanel customers = new JPanel();

        customers.setLayout(new BoxLayout(customers, BoxLayout.Y_AXIS));
        customers.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 100));


        JPanel cus_addrmPanel = new JPanel();
        cus_addrmPanel.setLayout(new BoxLayout(cus_addrmPanel, BoxLayout.X_AXIS));
        
        cus_newButton = new JButton("new");
        cus_newButton.setMaximumSize(new Dimension(80,30));
        cus_newButton.setActionCommand("cus_newPanel");
        cus_newButton.addActionListener(cardChangeListener);

        cus_removeButton = new JButton("remove");
        cus_removeButton.addActionListener(removeCustomerListener);
        cus_removeButton.setMaximumSize(new Dimension(80,30));
        cus_addrmPanel.add(cus_newButton);
        cus_addrmPanel.add(Box.createRigidArea(new Dimension(270,1)));
        cus_addrmPanel.add(cus_removeButton);

        String[] cus_columns = {"Name","Age","Gender","Contract Date"};
        cusTableModel = new DefaultTableModel(cus_columns, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return false;                                             //table edit false
            }
        };

        cusTable = new JTable(cusTableModel);
        cusTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableColumnModel cus_columnModel = (DefaultTableColumnModel) cusTable.getColumnModel();
        
        cusTableView();

		cus_columnModel.getColumn(0).setPreferredWidth(318);
        cus_columnModel.getColumn(1).setPreferredWidth(55);
        cus_columnModel.getColumn(2).setPreferredWidth(55);
        cus_columnModel.getColumn(3).setPreferredWidth(120);
        cusTable.setAutoCreateRowSorter(true);

        cusTable.getSelectionModel().addListSelectionListener(customerTableSelectedListener);

        JScrollPane cus_scrollPane = new JScrollPane(cusTable);
		cus_scrollPane.createVerticalScrollBar();
		cus_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel cus_lowPanel = new JPanel();
        cus_lowPanel.setLayout(new BoxLayout(cus_lowPanel, BoxLayout.X_AXIS));

        cus_openButton = new JButton("open");
        cus_openButton.setEnabled(false);
        cus_openButton.setMaximumSize(new Dimension(80,30));
        cus_openButton.setActionCommand("cus_openPanel");
        cus_openButton.addActionListener(cardChangeListener);
        cus_openButton.addActionListener(customerOpenListener);

        cus_lowPanel.add(Box.createRigidArea(new Dimension(487,1)));
        cus_lowPanel.add(cus_openButton);
        

        customers.add(Box.createRigidArea(new Dimension(1,70)));
        customers.add(cus_addrmPanel);
        customers.add(Box.createRigidArea(new Dimension(1,10)));
        customers.add(cus_scrollPane);
        customers.add(Box.createRigidArea(new Dimension(1,10)));
        customers.add(cus_lowPanel);


        /* USERS PANEL */

        JPanel users = new JPanel();
        users.setLayout(new BoxLayout(users, BoxLayout.Y_AXIS));
        users.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 100));


        JPanel wor_addrmPanel = new JPanel();
        wor_addrmPanel.setLayout(new BoxLayout(wor_addrmPanel, BoxLayout.X_AXIS));
        
        JButton wor_newButton = new JButton("new Admin");
        wor_newButton.setMaximumSize(new Dimension(80,30));
        wor_newButton.setActionCommand("wor_newPanel");
        wor_newButton.addActionListener(cardChangeListener);
        JButton wor_removeButton = new JButton("remove Admin");
        wor_removeButton.addActionListener(removeWorkerListener);
        wor_removeButton.setMaximumSize(new Dimension(80,30));
        wor_addrmPanel.add(wor_newButton);
        wor_addrmPanel.add(Box.createRigidArea(new Dimension(120,1)));
        wor_addrmPanel.add(wor_removeButton);
        wor_addrmPanel.add(Box.createRigidArea(new Dimension(240,1)));

        String[] wor_columns = {"UserName","Class"};
        worTableModel = new DefaultTableModel(wor_columns, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return false;                                             //table edit false
            }
        };

        worTable = new JTable(worTableModel);
        worTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableColumnModel wor_columnModel = (DefaultTableColumnModel) worTable.getColumnModel();
        
        worTableView();

		wor_columnModel.getColumn(0).setPreferredWidth(493);
        wor_columnModel.getColumn(1).setPreferredWidth(55);
        worTable.setAutoCreateRowSorter(true);

        JScrollPane wor_scrollPane = new JScrollPane(worTable);
		wor_scrollPane.createVerticalScrollBar();
		wor_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        users.add(Box.createRigidArea(new Dimension(1,70)));
        users.add(wor_addrmPanel);
        users.add(Box.createRigidArea(new Dimension(1,10)));
        users.add(wor_scrollPane);



        /* CHANGE PASSWORD PANEL */

        JPanel passwordChangePanel = new JPanel();
        JPanel cpass_fieldPanel = new JPanel();

        cpass_currentPasswordField = new JPasswordField(15);
        cpass_newPasswordField = new JPasswordField(15);
        cpass_confirmNewPasswordField = new JPasswordField(15);

        cpass_fieldPanel.setLayout(new BoxLayout(cpass_fieldPanel, BoxLayout.Y_AXIS));
        cpass_fieldPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        JButton changePassButton = new JButton("Change Password");
        changePassButton.setMaximumSize(new Dimension(200, 50));
        changePassButton.addActionListener(changePasswordListener);

        cpass_fieldPanel.add(Box.createRigidArea(new Dimension(1,120)));
        cpass_fieldPanel.add(new JLabel("Current Password"));
        cpass_fieldPanel.add(Box.createRigidArea(new Dimension(1,3)));
        cpass_fieldPanel.add(cpass_currentPasswordField);
        cpass_fieldPanel.add(Box.createRigidArea(new Dimension(1,10)));
        cpass_fieldPanel.add(new JLabel("New Password"));
        cpass_fieldPanel.add(Box.createRigidArea(new Dimension(1,3)));
        cpass_fieldPanel.add(cpass_newPasswordField);
        cpass_fieldPanel.add(Box.createRigidArea(new Dimension(1,10)));
        cpass_fieldPanel.add(new JLabel("Confirm New Password"));
        cpass_fieldPanel.add(Box.createRigidArea(new Dimension(1,3)));
        cpass_fieldPanel.add(cpass_confirmNewPasswordField);
        cpass_fieldPanel.add(Box.createRigidArea(new Dimension(1,10)));
        cpass_fieldPanel.add(changePassButton);
        
        passwordChangePanel.add(cpass_fieldPanel);

        /* CARD PANEL */
        cardPanel.setLayout(layout);
        cardPanel.add(loginPanel);
        cardPanel.add(home, "home");
        cardPanel.add(inventory, "inventory");
        cardPanel.add(inv_newPanel, "inv_newPanel");
        cardPanel.add(inv_openPanel, "inv_openPanel");
        cardPanel.add(customers,"customers");
        cardPanel.add(cus_newPanel, "cus_newPanel");
        cardPanel.add(cus_openPanel,"cus_openPanel");
        cardPanel.add(users,"users");
        cardPanel.add(wor_newPanel, "wor_newPanel");
        cardPanel.add(passwordChangePanel,"changePassword");
        
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        getContentPane().add(leftPanel, BorderLayout.LINE_START);
        getContentPane().add(northPanel, BorderLayout.PAGE_START);
        
    }

    public void invTableView(){
        invTableModel.setRowCount(0);
        for(int i = 0; i < Furniture.furnitureCounts.size(); i++){
            if(Furniture.furnitureCounts.get(i).getCount() > 0){
                Object[] tmpObjects = {Furniture.furnitureCounts.get(i).getName(),Furniture.furnitureCounts.get(i).getCount()};
                invTableModel.addRow(tmpObjects);
            }else{
                Furniture.furnitureCounts.remove(i);
            }
        }
    }

    public void cusTableView(){
        cusTableModel.setRowCount(0);
        
        if(isNowLoginAdmin){
            for(int i = 0; i < RentalSystem.customers.size(); i++){
                Object[] tmpObjects = {RentalSystem.customers.get(i).getName(),RentalSystem.customers.get(i).getAge(),RentalSystem.customers.get(i).getGender(),RentalSystem.customers.get(i).getContrateDate()};
                cusTableModel.addRow(tmpObjects);
            }
        }else{
            for(int i = 0; i < RentalSystem.customers.size(); i++){
                if(RentalSystem.customers.get(i).getName().equals(nowLoginUserName)){
                    Object[] tmpObjects = {RentalSystem.customers.get(i).getName(),RentalSystem.customers.get(i).getAge(),RentalSystem.customers.get(i).getGender(),RentalSystem.customers.get(i).getContrateDate()};
                    cusTableModel.addRow(tmpObjects);
                }
            }
        }
    }

    public void worTableView(){
        worTableModel.setRowCount(0);
        for(int i = 0; i < RentalSystem.users.size(); i++){
            String rank;
            if(RentalSystem.users.get(i).getAdminster()){
                rank = "Admin";
            }else{
                rank = "Customer";
            }
            Object[] tmpObjects = {RentalSystem.users.get(i).getName(),rank};
            worTableModel.addRow(tmpObjects);
        }
    }

    public void cusOpenTableView(){
        cusOpen_cusTableModel.setRowCount(0);
        cusOpen_invTableModel.setRowCount(0);
        cusOpen_customerIndex = -1;
        for(int i = 0; i < RentalSystem.customers.size(); i++){
            if(RentalSystem.customers.get(i).getAllField().equals(cus_tableAllField)){
                
                cusOpen_customerIndex = i;
                break;
            }
        }
        if(RentalSystem.customers.get(cusOpen_customerIndex).getCustomerRentalCount() != null){
            for(int i = 0; i < RentalSystem.customers.get(cusOpen_customerIndex).getCustomerRentalCount().size(); i++){
                Object[] tmpObjects = {RentalSystem.customers.get(cusOpen_customerIndex).getCustomerRentalCount().get(i).getName(),RentalSystem.customers.get(cusOpen_customerIndex).getCustomerRentalCount().get(i).getCount()};
                if(RentalSystem.customers.get(cusOpen_customerIndex).getCustomerRentalCount().get(i).getCount() > 0)
                    cusOpen_cusTableModel.addRow(tmpObjects);
            }
        }

        if(Furniture.furnitureCounts != null){
            for(int i = 0; i < Furniture.furnitureCounts.size(); i++){
                Object[] tmpObjects = {Furniture.furnitureCounts.get(i).getName(),Furniture.furnitureCounts.get(i).getRendableCount()};
                if(Furniture.furnitureCounts.get(i).getRendableCount() > 0)
                    cusOpen_invTableModel.addRow(tmpObjects);
            }
        }
    }

    public void invOpenTableView(){
        invOpen_invTableModel.setRowCount(0);
        invOpen_cusTableModel.setRowCount(0);
        invOpen_furnitureIndex = -1;

        for(int i = 0; i < Furniture.furnitureCounts.size(); i++){
            if(Furniture.furnitureCounts.get(i).getName().equals(inv_furnitureName)){
                invOpen_furnitureIndex = i;
                break;
            }
        }
        for(int i = 0; i < RentalSystem.customers.size(); i++){
            for(int j = 0; j < RentalSystem.customers.get(i).getCustomerRentalCount().size(); j++){
                if(RentalSystem.customers.get(i).getCustomerRentalCount().get(j).getName().equals(inv_furnitureName)){
                    Object[] tmpObjects = {RentalSystem.customers.get(i).getName(),RentalSystem.customers.get(i).getAge(),RentalSystem.customers.get(i).getGender(),RentalSystem.customers.get(i).getContrateDate(),RentalSystem.customers.get(i).getCustomerRentalCount().get(j).getCount()};
                    if(RentalSystem.customers.get(i).getCustomerRentalCount().get(j).getCount() > 0)
                        invOpen_invTableModel.addRow(tmpObjects);
                    break;
                }
            }
            if(i + 1 == RentalSystem.customers.size()){
                Object[] tmpObjects ={"","","","",Furniture.furnitureCounts.get(invOpen_furnitureIndex).getRendableCount()};
                if(Furniture.furnitureCounts.get(invOpen_furnitureIndex).getRendableCount() > 0)
                    invOpen_invTableModel.addRow(tmpObjects);
                break;
            }
        }

        if(RentalSystem.customers.size() == 0){
            Object[] tmpObjects ={"","","","",Furniture.furnitureCounts.get(invOpen_furnitureIndex).getRendableCount()};
                if(Furniture.furnitureCounts.get(invOpen_furnitureIndex).getRendableCount() > 0)
                    invOpen_invTableModel.addRow(tmpObjects);
        }
        
        if(isNowLoginAdmin){
            for(int i = 0; i < RentalSystem.customers.size(); i++){
                Object[] tmpObjects = {RentalSystem.customers.get(i).getName(),RentalSystem.customers.get(i).getAge(),RentalSystem.customers.get(i).getGender(),RentalSystem.customers.get(i).getContrateDate()};
                invOpen_cusTableModel.addRow(tmpObjects);
            }
        }else{
            for(int i = 0; i < RentalSystem.customers.size(); i++){
                if(RentalSystem.customers.get(i).getName().equals(nowLoginUserName)){
                    Object[] tmpObjects = {RentalSystem.customers.get(i).getName(),RentalSystem.customers.get(i).getAge(),RentalSystem.customers.get(i).getGender(),RentalSystem.customers.get(i).getContrateDate()};
                    invOpen_cusTableModel.addRow(tmpObjects);
                }
            }
        }
    }

    public static String getTime(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timeStr = sdf.format(timestamp);
        return timeStr;
    }

    public static void lastLog(){
        if(!(nowLoginUserName == null)) log.add(getTime() + "   " + nowLoginUserName + " Logout");
    }
}
