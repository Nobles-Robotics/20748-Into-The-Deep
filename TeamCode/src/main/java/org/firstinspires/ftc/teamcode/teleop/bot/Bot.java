
package org.firstinspires.ftc.teamcode.teleop.bot;

import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

@Slides.Attach
public class Bot{
    public static Command lift3(){
        return new Sequential(Slides.goTo(10000));
    }
}

