/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright 2013 Redwarp
 */
package net.redwarp.tool.resizer.views;

import net.redwarp.tool.resizer.misc.Localization;
import net.redwarp.tool.resizer.misc.Preferences;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AboutDialog extends JDialog {
    private static final long serialVersionUID = 7783865044667012251L;

    public AboutDialog(JFrame parent) {
        this.setResizable(false);
        this.setSize(new Dimension(430, 270));
        this.getContentPane().setLayout(new BorderLayout(0, 0));

        JLabel lblResizer = new JLabel(Localization.get("app_name") + " "
                + Preferences.getVersion());
        lblResizer.setBorder(new EmptyBorder(10, 10, 10, 10));
        lblResizer.setVerticalTextPosition(SwingConstants.BOTTOM);
        lblResizer.setIconTextGap(10);
        lblResizer.setFont(lblResizer.getFont().deriveFont(
                lblResizer.getFont().getStyle() | Font.BOLD, 16f));
        lblResizer.setBackground(Color.WHITE);
        lblResizer.setIcon(new ImageIcon(AboutDialog.class
                .getResource("/img/icon_64.png")));
        this.getContentPane().add(lblResizer, BorderLayout.NORTH);

        JTextArea txtrResizerIsA = new JTextArea();
        txtrResizerIsA.setEditable(false);
        txtrResizerIsA.setWrapStyleWord(true);
        txtrResizerIsA.setBorder(new EmptyBorder(0, 20, 20, 20));
        txtrResizerIsA.setFont(UIManager.getFont("Label.font"));
        txtrResizerIsA.setBackground(Color.WHITE);
        txtrResizerIsA.setLineWrap(true);
        txtrResizerIsA.setText(Localization.get("about_text"));
        this.getContentPane().add(txtrResizerIsA, BorderLayout.CENTER);
        this.getContentPane().setBackground(Color.WHITE);

        this.setLocationRelativeTo(parent);
    }

}
