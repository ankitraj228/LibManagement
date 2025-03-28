import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class DigitalLibraryGUI {
    private ArrayList<Book> books = new ArrayList<>();
    private JFrame frame;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DigitalLibraryGUI().initialize());
    }

    private void initialize() {
        // Create main frame
        frame = new JFrame("Digital Library Management System");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Changed for custom close handling
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Handle window close event
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitSystem(null);
            }
        });

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this::exitSystem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton(buttonPanel, "Add Book", this::addBook);
        addButton(buttonPanel, "View All", this::viewAllBooks);
        addButton(buttonPanel, "Search", this::searchBook);
        addButton(buttonPanel, "Update", this::updateBook);
        addButton(buttonPanel, "Delete", this::deleteBook);
        addButton(buttonPanel, "Exit", this::exitSystem);
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Create table for displaying books
        String[] columnNames = {"ID", "Title", "Author", "Genre", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Status bar
        JLabel statusBar = new JLabel(" Ready");
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addButton(JPanel panel, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void addBook(ActionEvent e) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "Checked Out"});

        panel.add(new JLabel("Book ID:"));
        panel.add(idField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add New Book",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();

            // Validate inputs
            if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "ID, Title, and Author cannot be empty",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isBookIdExists(id)) {
                JOptionPane.showMessageDialog(frame, "Book ID already exists",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book newBook = new Book(id, title, author, genre, status);
            books.add(newBook);
            updateTable();
            JOptionPane.showMessageDialog(frame, "Book added successfully!");
        }
    }

    private void viewAllBooks(ActionEvent e) {
        updateTable();
    }

    private void searchBook(ActionEvent e) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField searchField = new JTextField();
        JComboBox<String> searchType = new JComboBox<>(new String[]{"ID", "Title"});

        panel.add(new JLabel("Search by:"));
        panel.add(searchType);
        panel.add(new JLabel("Search term:"));
        panel.add(searchField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Search Book",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String searchTerm = searchField.getText().trim();
            String type = (String) searchType.getSelectedItem();

            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a search term",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<Book> foundBooks = new ArrayList<>();
            if (type.equals("ID")) {
                Book book = findBookById(searchTerm);
                if (book != null) foundBooks.add(book);
            } else {
                foundBooks = findBooksByTitle(searchTerm);
            }

            if (foundBooks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No books found",
                        "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                displaySearchResults(foundBooks);
            }
        }
    }

    private void updateBook(ActionEvent e) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a book to update",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Book book = findBookById(id);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField genreField = new JTextField(book.getGenre());
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "Checked Out"});
        statusCombo.setSelectedItem(book.getStatus());

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Update Book",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();

            if (title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Title and Author cannot be empty",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setStatus(status);
            updateTable();
            JOptionPane.showMessageDialog(frame, "Book updated successfully!");
        }
    }

    private void deleteBook(ActionEvent e) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a book to delete",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Book book = findBookById(id);

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this book?\n" + book.getTitle(),
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            books.remove(book);
            updateTable();
            JOptionPane.showMessageDialog(frame, "Book deleted successfully!");
        }
    }

    private void exitSystem(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to exit the Digital Library System?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Here you could add code to save data before exiting
            // For example: saveBooksToFile();

            frame.dispose(); // Properly close the window
            System.exit(0);  // Terminate the program
        }
    }

    // Helper methods
    private boolean isBookIdExists(String id) {
        return findBookById(id) != null;
    }

    private Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    private ArrayList<Book> findBooksByTitle(String title) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Book book : books) {
            Object[] rowData = {
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getStatus()
            };
            tableModel.addRow(rowData);
        }
    }

    private void displaySearchResults(ArrayList<Book> foundBooks) {
        DefaultTableModel searchModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Author", "Genre", "Status"}, 0);

        for (Book book : foundBooks) {
            Object[] rowData = {
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getStatus()
            };
            searchModel.addRow(rowData);
        }

        JTable searchTable = new JTable(searchModel);
        JOptionPane.showMessageDialog(frame, new JScrollPane(searchTable),
                "Search Results", JOptionPane.PLAIN_MESSAGE);
    }
}