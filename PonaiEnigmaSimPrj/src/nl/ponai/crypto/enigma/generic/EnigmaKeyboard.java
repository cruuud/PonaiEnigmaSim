package nl.ponai.crypto.enigma.generic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class EnigmaKeyboard extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4529005119341570399L;
	private JFrame m_frame;
	private EnigmaMachine m_machine;
	private JPanel m_pnlKeyboard;
	private JTextArea m_txtMessage;	
	private EnigmaSimulator m_simulator;
	private boolean m_bTypingEnabled=true;
	private JLabel m_lblPosition;
	
	private JButton m_btnEncipher;
	private JButton m_btnReset;
	
	public EnigmaKeyboard(EnigmaSimulator simulator,EnigmaMachine machine)
	{
		m_machine=machine;
		m_simulator=simulator;
		m_frame=new JFrame("Enigma Keyboard");
		m_frame.getContentPane().setLayout(new BorderLayout());
		m_frame.setTitle("Enigma keyboard");
		m_frame.getContentPane().setLayout(new BorderLayout());		
		m_frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		m_frame.addWindowListener(new WindowAdapter()
		{
						
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				// TODO Auto-generated method stub
				m_simulator.closedKeyboard();				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0)
			{				
				// TODO Auto-generated method stub
				m_simulator.closedKeyboard();
				
			}
		});
		addComponents();			
		m_frame.pack();
		centerFrame();		
		m_frame.setVisible(true);		
	}
	
	public void enableTyping()
	{
		m_frame.setTitle("Enigma keyboard - Ready!");
		m_bTypingEnabled=true;
	}
	
	public void disableTyping()
	{
		m_frame.setTitle("Enigma keyboard - Machine is open!");
		m_bTypingEnabled=false;
	}
	
	private void centerFrame()
	{
		Dimension mainDimension=m_frame.getSize();
		Point location=new Point(Toolkit.getDefaultToolkit().getScreenSize().width/2-mainDimension.width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2-mainDimension.height/2);
		m_frame.setSize(mainDimension);
		m_frame.setLocation(location);		
	}
	
	private void addComponents()
	{		
		m_txtMessage=new JTextArea(16,32);
		m_txtMessage.setBackground(Color.black);
		m_txtMessage.setForeground(Color.white);
		m_txtMessage.setCaretColor(Color.yellow);
		String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int i=0;i<alphabet.length();i++)
		{
			m_txtMessage.getInputMap().put(KeyStroke.getKeyStroke(alphabet.charAt(i)), "doKeyboardKeyAction");
			m_txtMessage.getInputMap().put(KeyStroke.getKeyStroke((""+alphabet.charAt(i)).toLowerCase().charAt(0)), "doKeyboardKeyAction");
			m_txtMessage.getActionMap().put("doKeyboardKeyAction", this);
		}
		
		JScrollPane pneScroll=new JScrollPane(m_txtMessage,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pneScroll.setBorder(BorderFactory.createLoweredBevelBorder());
		
		m_pnlKeyboard=new JPanel(new BorderLayout());
		m_pnlKeyboard.setBorder(BorderFactory.createTitledBorder("Type your message"));
		m_pnlKeyboard.add(pneScroll,BorderLayout.CENTER);
		
		
		m_frame.getContentPane().add(m_pnlKeyboard,BorderLayout.CENTER);	
	
		m_lblPosition=new JLabel(m_machine.getPositionString(),JLabel.CENTER);
		m_lblPosition.setOpaque(true);
		m_lblPosition.setBackground(Color.black);
		m_lblPosition.setForeground(Color.green);
		m_lblPosition.setBorder(BorderFactory.createLoweredBevelBorder());
		m_lblPosition.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		JPanel pnlPosition=new JPanel();
		pnlPosition.setBorder(BorderFactory.createRaisedBevelBorder());
		pnlPosition.add(m_lblPosition);
		m_btnReset=new JButton("Reset");
		m_btnReset.setMnemonic('r');
		m_btnReset.addActionListener(this);
		pnlPosition.add(m_btnReset);
		m_frame.getContentPane().add(pnlPosition,BorderLayout.NORTH);
		
		m_btnEncipher=new JButton("Encipher");
		m_btnEncipher.setMnemonic('e');
		m_btnEncipher.addActionListener(this);
		
		

		JPanel pnlButton=new JPanel();
		pnlButton.add(m_btnEncipher);
		m_frame.getContentPane().add(pnlButton,BorderLayout.SOUTH);
	}		
	
	public void actionPerformed( ActionEvent tf )
	{
		if(tf.getSource()==m_txtMessage && m_bTypingEnabled)
		{
			char key=tf.getActionCommand().charAt(0);
			
			if(key>='a' && key<='z')
			{
				key=m_machine.processChar((""+key).toUpperCase().charAt(0));
				key=(""+key).toLowerCase().charAt(0);
			}
			else
			{
				key=m_machine.processChar(key);
			}
			int selstart=m_txtMessage.getSelectionStart();
			int selend=m_txtMessage.getSelectionEnd();
			
			String str=m_txtMessage.getText();
			if(selstart>=0 && selend>selstart)
			{	
				int posCaret=m_txtMessage.getCaretPosition();
				if(selstart>0)
				{
					str=str.substring(0,selstart)+key+str.substring(selend);
				}
				else
				{
					str=key+str.substring(selend);
				}
				m_txtMessage.setText(str);	
				if(str.length()>posCaret)m_txtMessage.setCaretPosition(posCaret+1);
			}
			else
			{
				int posCaret=m_txtMessage.getCaretPosition();
				
				if(posCaret>=0)
				{
					str=str.substring(0,posCaret)+key+str.substring(posCaret);
				}
				else
				{
					str+=key;
				}
		
				m_txtMessage.setText(str);	
				if(str.length()>posCaret)m_txtMessage.setCaretPosition(posCaret+1);
			}
			m_lblPosition.setText(m_machine.getPositionString());
		}
		else if(tf.getSource()==m_btnEncipher)
		{
			String txt=m_txtMessage.getText().trim();			
			StringBuffer sb=new StringBuffer();			
			for(int i=0;i<txt.length();i++)
			{
				char ch=txt.charAt(i);
				boolean bUppercase=(ch>='A' && ch<='Z');
				if(bUppercase)
				{
					ch=m_machine.processChar(ch);
					sb.append(ch);
				}
				else if(ch>='a' && ch<='z')
				{
					ch=m_machine.processChar((""+ch).toUpperCase().charAt(0));
					sb.append((""+ch).toLowerCase());
				}				
				else
				{
					sb.append(ch);
				}
			}
			
			m_txtMessage.setText(sb.toString());
			m_lblPosition.setText(m_machine.getPositionString());
		}
		else if(tf.getSource()==m_btnReset) 
		{
			m_machine.resetPositions();
			m_lblPosition.setText(m_machine.getPositionString());
		}
	}
	
	public void updatePosition(final String position)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				m_lblPosition.setText(position);
			}
		});
	}
	
	public void dispose()
	{
		m_frame.dispose();
	}
}
