package principal;

import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import lexico.LexicalAnalyser;
import runtime.Executer;
import sintactico.SyntacticAnalyzer;

public class GUI extends javax.swing.JFrame {
    private BufferedWriter myBufferedWriter;
    private SyntacticAnalyzer mySyntacticAnalyzer;
    private Executer myExecuter;
    private LexicalAnalyser myLexicalAnalyser;
    
    public GUI() {
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("hppc_icon.png")));
        myLexicalAnalyser = new LexicalAnalyser();
        try {
            workArea.setText(myLexicalAnalyser.readFile());            
        } catch (Exception e) {
        }
    }//end constructor

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        workArea = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        compile_button = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        run_button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        workArea.setColumns(20);
        workArea.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        workArea.setLineWrap(true);
        workArea.setRows(5);
        workArea.setToolTipText("");
        jScrollPane1.setViewportView(workArea);

        console.setEditable(false);
        console.setColumns(20);
        console.setRows(5);
        jScrollPane2.setViewportView(console);

        jTabbedPane1.addTab("console", jScrollPane2);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel1.setText("compile");
        jLabel1.setAlignmentY(0.0F);
        jToolBar1.add(jLabel1);

        compile_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/compile_btn.png"))); // NOI18N
        compile_button.setAlignmentY(0.0F);
        compile_button.setBorder(null);
        compile_button.setBorderPainted(false);
        compile_button.setFocusable(false);
        compile_button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        compile_button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        compile_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compile_buttonActionPerformed(evt);
            }
        });
        jToolBar1.add(compile_button);

        jLabel2.setText("run");
        jLabel2.setAlignmentY(0.0F);
        jToolBar1.add(jLabel2);

        run_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/run_btn.png"))); // NOI18N
        run_button.setAlignmentY(0.0F);
        run_button.setBorder(null);
        run_button.setFocusable(false);
        run_button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        run_button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        run_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                run_buttonActionPerformed(evt);
            }
        });
        jToolBar1.add(run_button);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("");
        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void run_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_run_buttonActionPerformed
    try {
            mySyntacticAnalyzer = new SyntacticAnalyzer();
            myBufferedWriter = new BufferedWriter(new FileWriter("sourceCode.txt"));
            myExecuter = new Executer();
            String input = workArea.getText();
            String output = "";
            myBufferedWriter.write(input, 0, input.length());
            myBufferedWriter.flush();
            myBufferedWriter.close();
            mySyntacticAnalyzer.analyzeLexic();
            myExecuter.loadFile();
            output += myExecuter.execute()+"\n";
            output += mySyntacticAnalyzer.getStatus();
            console.setText(output);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            console.setText(mySyntacticAnalyzer.getStatus());
        }  
    }//GEN-LAST:event_run_buttonActionPerformed

    private void compile_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compile_buttonActionPerformed
        try {
            mySyntacticAnalyzer = new SyntacticAnalyzer();
            myBufferedWriter = new BufferedWriter(new FileWriter("sourceCode.txt"));
            String input = workArea.getText();
            myBufferedWriter.write(input, 0, input.length());
            myBufferedWriter.flush();
            myBufferedWriter.close();
            mySyntacticAnalyzer.analyzeLexic();
            console.setText(mySyntacticAnalyzer.getStatus());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            console.setText(mySyntacticAnalyzer.getStatus());
        }
    }//GEN-LAST:event_compile_buttonActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Default".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }//end main

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton compile_button;
    private javax.swing.JTextArea console;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton run_button;
    private javax.swing.JTextArea workArea;
    // End of variables declaration//GEN-END:variables
}//end class