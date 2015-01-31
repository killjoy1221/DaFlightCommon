/*
 * Copyright (c) 2014, dags_ <dags@dags.me>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.dags.daflight.gui.hud;

import me.dags.daflight.DaFlight;
import me.dags.daflight.minecraft.Colour;
import me.dags.daflight.player.DaPlayer;
import me.dags.daflight.utils.Config;
import me.dags.daflightapi.ui.DaFlightUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders text to screen when certain components of the mod are enabled
 *
 * @author dags_
 */
public class HUD implements DaFlightUI
{

    private List<DFEntry> mods;
    private int counter = 50;

    private String flight = "f";
    private String cine = "c";
    private String run = "r";
    private String modifier = "*";
    private String fb = "fb";

    public HUD()
    {
        mods = new ArrayList<DFEntry>();
        mods.add(0, new DFEntry("", false));
        mods.add(1, new DFEntry("", false));
        mods.add(2, new DFEntry(Colour.addColour(Config.getInstance().fullBrightStatus), false));
        mods.add(3, new DFEntry("", false));
    }

    public void updateMsg()
    {
        DaPlayer dp = DaFlight.get().daPlayer;
        boolean flyModifier = false;
        // Flight
        if (dp.flyModOn || DaPlayer.KEY_BINDS.enableFly.keyHeld())
        {
            String s = "";
            flyModifier = DaPlayer.KEY_BINDS.speedModifier.keyHeld();
            if (dp.flyModOn)
            {
                s = flight;
                if (dp.cineFlightOn)
                {
                    s = cine;
                }
            }
            if (DaPlayer.DF_PERMISSIONS.flyEnabled() && (dp.flySpeed.isBoosting() || flyModifier))
            {
                s = s + modifier;
            }
            mods.get(0).setTitle(s);
            mods.get(0).setShow(true);
        }
        else
        {
            mods.get(0).setShow(false);
        }
        // Sprint
        if (DaPlayer.DF_PERMISSIONS.sprintEnabled() && (dp.sprintModOn || DaPlayer.KEY_BINDS.enableSprint.keyHeld()))
        {
            String s = run;
            if (dp.sprintSpeed.isBoosting() || (DaPlayer.KEY_BINDS.speedModifier.keyHeld() && !flyModifier))
            {
                s = s + modifier;
            }
            mods.get(1).setTitle(s);
            mods.get(1).setShow(true);
        }
        else
        {
            mods.get(1).setShow(false);
        }
        // FullBright
        mods.get(2).setTitle(fb);
        mods.get(2).setShow(dp.fullBrightOn);
    }

    public void refreshStatuses()
    {
        flight = Colour.getColouredString(Config.getInstance().flightStatus);
        cine = Colour.getColouredString(Config.getInstance().cineFlightStatus);
        run = Colour.getColouredString(Config.getInstance().runStatus);
        modifier = Colour.getColouredString(Config.getInstance().speedStatus);
        fb = Colour.getColouredString(Config.getInstance().fullBrightStatus);
    }

    public void renderTemp(String s)
    {
        counter = 50;
        mods.get(3).setTitle(s);
        mods.get(3).setShow(true);
    }

    public void setTemp(Boolean b)
    {
        mods.get(3).setShow(b);
    }

    @Override
    public void draw()
    {
        if (Config.getInstance().disabled)
        {
            return;
        }
        counter--;
        if (Config.getInstance().showHud && DaFlight.getMC().getMinecraft().inGameHasFocus && !DaFlight.getMC().getGameSettings().showDebugInfo)
        {
            int slot = 5;
            for (DFEntry d : mods)
            {
                if (d.isShown())
                {
                    if (Config.getInstance().textShadow)
                    {
                        DaFlight.getMC().getMinecraft().fontRendererObj.drawStringWithShadow(d.getTitle(), 5, slot, 0xFFFFFF);
                    }
                    else
                    {
                        DaFlight.getMC().getMinecraft().fontRendererObj.drawString(d.getTitle(), 5, slot, 0xFFFFFF);
                    }
                    slot += 10;
                }
            }
        }
        if (counter <= 0)
        {
            setTemp(false);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public int addModStatus(String s)
    {
        int i = mods.size();
        mods.add(i, new DFEntry(s, false));
        return i;
    }

    @SuppressWarnings("unused")
    @Override
    public void removeModStatus(int id)
    {
        if (id < mods.size() && id > 3)
        {
            mods.remove(id);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void setStatusVisibility(int id, boolean b)
    {
        if (id < mods.size() && id > 3)
        {
            mods.get(id).setShow(b);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void setStatus(int id, String s)
    {
        if (id < mods.size() && id > 3)
        {
            mods.get(id).setTitle(s);
        }
    }

}