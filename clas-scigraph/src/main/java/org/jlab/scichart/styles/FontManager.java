/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.styles;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 *
 * @author gavalian
 */
public class FontManager {
 
    public FontManager(){
        
    }
    
    public Font getFont(String name, int size){
        //return new Font("Gill Sans",Font.PLAIN,size);
        //return new Font("Helvetica Neue",Font.PLAIN,size);
        return new Font("Lucida Bright",Font.BOLD,size);
    }
    
    public void list(){
        String fonts[] = 
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        for ( int i = 0; i < fonts.length; i++ )
        {
            System.out.println(fonts[i]);
        }
    }
}
