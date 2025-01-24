
package org.firstinspires.ftc.teamcode.teleop.bot;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Race;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.util.BulkReads;
import org.firstinspires.ftc.teamcode.util.SilkRoad;

@Mercurial.Attach
@Drive.Attach
@Slides.Attach
@BulkReads.Attach
@Gripper.Attach
@Arm.Attach
@SilkRoad.Attach
public class Bot{
    public static Command attachSpecimen(){
        return new Sequential(
                new Race(Slides.goTo(3000), Gripper.close()),
                new Race(Slides.goTo(0), Gripper.close()));
    }
}

