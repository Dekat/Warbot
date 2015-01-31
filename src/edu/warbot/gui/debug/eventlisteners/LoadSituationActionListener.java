package edu.warbot.gui.debug.eventlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import edu.warbot.gui.debug.DebugModeToolBar;
import edu.warbot.gui.launcher.WarLauncherInterface;
import edu.warbot.launcher.SituationLoader;

public class LoadSituationActionListener implements ActionListener {

	private WarLauncherInterface _launcherInterface;

	public LoadSituationActionListener(WarLauncherInterface launcherInterface) {
		_launcherInterface = launcherInterface;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "*" + DebugModeToolBar.SITUATION_FILES_EXTENSION;
			}

			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(DebugModeToolBar.SITUATION_FILES_EXTENSION);
			}
		});
		int returnVal = fc.showOpenDialog(_launcherInterface);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String fileName = file.toString();
			if (!fileName.endsWith(DebugModeToolBar.SITUATION_FILES_EXTENSION))
				file = new File(fileName + DebugModeToolBar.SITUATION_FILES_EXTENSION);
			_launcherInterface.getGameSettings().setSituationLoader(new SituationLoader(file));
			_launcherInterface.startGame();
		}
	}

}
