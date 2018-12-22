package org.gitee.lidevin.image2base64.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.gitee.lidevin.image2base64.utils.ImageUtil;
import org.gitee.lidevin.image2base64.utils.ImageUtil.ImageType;

/**
 * 设计窗口
 * 
 * @author LDSH
 */
public class MainWindow {

	/**
	 * 主窗体
	 */
	private JFrame frame;
	/**
	 * base64码文本域
	 */
	private JTextArea base64Tf;
	/**
	 * 文件路径输入域
	 */
	private JTextField fileSrcTf;
	/**
	 * 显示图片
	 */
	private JLabel img;

	/**
	 * 正确信息颜色
	 */
	private static final Color success = new Color(0, 200, 0);
	/**
	 * 错误信息颜色
	 */
	private static final Color error = new Color(250, 0, 0);

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 510, 411);
		frame.setResizable(false);
		frame.setTitle("图片和base64转换器");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton youdaoyunBtn = new JButton("从剪切板中获取图片的base64(有道云笔记)");
		youdaoyunBtn.setBounds(0, 0, 504, 23);
		frame.getContentPane().add(youdaoyunBtn);

		JLabel lblNewLabel = new JLabel("图片");
		lblNewLabel.setBounds(0, 33, 57, 15);
		frame.getContentPane().add(lblNewLabel);

		
		base64Tf = new JTextArea();
		base64Tf.setBounds(213, 91, 291, 291);
		base64Tf.setColumns(10);
		base64Tf.setEditable(false);
		base64Tf.setBackground(Color.WHITE);
		base64Tf.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		base64Tf.setDisabledTextColor(Color.BLACK);
		base64Tf.setLineWrap(true);
		base64Tf.setWrapStyleWord(true);
		JScrollPane base64TfWrap = new JScrollPane(base64Tf);
		base64TfWrap.setBounds(213, 91, 291, 291);
		frame.getContentPane().add(base64TfWrap);

		JLabel lblBase = new JLabel("base64");
		lblBase.setBounds(450, 33, 54, 15);
		frame.getContentPane().add(lblBase);

		JPanel panel = new JPanel();
		panel.setBounds(0, 58, 214, 23);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JButton openFileBtn = new JButton(". . .");
		openFileBtn.setBackground(Color.lightGray);
		openFileBtn.setBounds(181, -1, 33, 23);
		panel.add(openFileBtn);

		fileSrcTf = new JTextField();
		fileSrcTf.setBounds(0, 0, 183, 21);
		fileSrcTf.setEnabled(false);
		fileSrcTf.setDisabledTextColor(Color.BLACK);
		fileSrcTf.setColumns(10);
		panel.add(fileSrcTf);
		
		img = new JLabel();
		img.setBounds(0, 91, 214, 291);
		frame.getContentPane().add(img);

		JButton toImgBtn = new JButton("<< 转成图片");
		toImgBtn.setBounds(50, 29, 114, 23);
		frame.getContentPane().add(toImgBtn);

		JButton toBase64Btn = new JButton("转成base64 >>");
		toBase64Btn.setBounds(295, 29, 131, 23);
		frame.getContentPane().add(toBase64Btn);

		JButton coypBtn = new JButton("复制");
		coypBtn.setBounds(224, 58, 63, 23);
		frame.getContentPane().add(coypBtn);

		JButton pasteBtn = new JButton("粘贴");
		pasteBtn.setBounds(295, 58, 63, 23);
		frame.getContentPane().add(pasteBtn);

		JLabel info = new JLabel();
		info.setBounds(366, 66, 138, 15);
		frame.getContentPane().add(info);
		frame.setVisible(true);

		// 添加事件
		youdaoyunBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Image image = ImageUtil.getClipboardImage();
					if (image == null) {
						info.setText("粘贴板没有获取到图片");
						info.setForeground(error);
						return;
					}
					String base64 = ImageUtil.imageToBase64(image, ImageType.PNG);
					ImageUtil.textWrite(ImageUtil.getYoudaoyunImageUrl(base64));
				} catch (Exception e) {
					info.setText("程序异常");
					info.setForeground(error);
					return;
				}
				info.setText("已经复制到粘贴板");
				info.setForeground(success);
			}
		});
		coypBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageUtil.textWrite(base64Tf.getText());
				info.setText("复制成功");
				info.setForeground(success);
			}
		});
		pasteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				base64Tf.setText(ImageUtil.getClipboardText());
				info.setText("粘贴成功");
				info.setForeground(success);
			}
		});
		toImgBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String base64 = base64Tf.getText();
				if(!base64.matches("image/.*;base64,.*")) {
					info.setText("base64格式错误");
					info.setForeground(error);
				}
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("保存");
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filePath = chooser.getSelectedFile().getPath();
					ImageUtil.base64ToFile(base64, filePath);
					fileSrcTf.setText(filePath);
					info.setText("保存成功");
					info.setForeground(success);
				}
			}
		});
		toBase64Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = fileSrcTf.getText();
				if("".equals(name.trim())) {
					info.setText("请选择文件");
					info.setForeground(error);
					return;
				}
				File file = new File(name);
				if(file.isDirectory()) {
					info.setText("请选择文件");
					info.setForeground(error);
					return;
				}
				try {
					base64Tf.setText(ImageUtil.fileToBase64(file));
				} catch (IOException e1) {
					info.setText("转换失败");
					info.setForeground(error);
					return ;
				}
				info.setText("转换成功");
				info.setForeground(success);
			}
		});
		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("常用图片", "jpg", "jpeg", "png", "ico");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filePath = chooser.getSelectedFile().getPath();
					fileSrcTf.setText(filePath);
					img.setIcon(new ImageIcon(filePath));
				}
			}
		});
	}

	public void display() {
		initialize();
	}
}
