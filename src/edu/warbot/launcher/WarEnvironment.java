package edu.warbot.launcher;

import madkit.kernel.MadkitClassLoader;
import turtlekit.agr.TKOrganization;
import turtlekit.kernel.Patch;
import turtlekit.kernel.TKEnvironment;
import turtlekit.kernel.TurtleKit;

import static turtlekit.kernel.TurtleKit.Option.patch;

public class WarEnvironment extends TKEnvironment {

    private Patch patch;

    public WarEnvironment() {
        super();
        patch = new Patch();
    }

    @Override
    protected void initPatchGrid() {
        // Override initPatchGrid to disabled patchs
    }

    @Override
    protected Patch getPatch(int i, int j) {
        return patch;
    }


}
