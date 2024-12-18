package org.firstinspires.ftc.teamcode.util;

public class  MotionProfiler {

    public MotionProfiler(double max_velocity, double max_acceleration){
        this.max_acceleration = max_acceleration;
        this.max_velocity = max_velocity;
    }
    private final double max_velocity, max_acceleration;
    private boolean isOver = true;
    private boolean isDone = false;
    private double temp_max_accel, temp_max_vel;
    private double start_pos, final_pos, distance, acceleration_dt, halfway_distance, acceleration_distance, new_max_velocity, deacceleration_dt, cruise_distance, cruise_dt, deacceleration_time, entire_dt;

    public void init_new_profile(double start_pos, double final_pos){
        this.start_pos = start_pos;
        this.final_pos = final_pos;
        isOver = false;

        distance = final_pos-start_pos;

        if(distance < 0){
            temp_max_vel = -max_velocity;
            temp_max_accel = -max_acceleration;
        }else{
            temp_max_accel = max_acceleration;
            temp_max_vel = max_velocity;
        }

        acceleration_dt = temp_max_vel / temp_max_accel;

        halfway_distance = distance / 2;
        acceleration_distance = 0.5 * temp_max_accel * acceleration_dt * acceleration_dt;

        if (Math.abs(acceleration_distance) > Math.abs(halfway_distance)) {
            acceleration_dt = Math.sqrt(Math.abs(halfway_distance / (0.5 * temp_max_accel)));
        }
        acceleration_distance = 0.5 * temp_max_accel * acceleration_dt * acceleration_dt;

        new_max_velocity = temp_max_accel * acceleration_dt;

        deacceleration_dt = acceleration_dt;

        cruise_distance = distance - 2 * acceleration_distance;
        cruise_dt = cruise_distance / new_max_velocity;
        deacceleration_time = acceleration_dt + cruise_dt;

        entire_dt = acceleration_dt + cruise_dt + deacceleration_dt;
    }


    public double motion_profile_pos(double current_dt) {

        if (current_dt > entire_dt) {
            isOver = true;
            isDone = true;
            return final_pos;
        }

        if (current_dt < acceleration_dt)
            return start_pos + 0.5 * temp_max_accel * current_dt * current_dt;

        else if (current_dt < deacceleration_time) {
            acceleration_distance = 0.5 * temp_max_accel * acceleration_dt * acceleration_dt;
            double cruise_current_dt = current_dt - acceleration_dt;

            return start_pos + acceleration_distance + new_max_velocity * cruise_current_dt;
        }

        else {
            acceleration_distance = 0.5 * temp_max_accel * acceleration_dt * acceleration_dt;
            cruise_distance = new_max_velocity * cruise_dt;
            deacceleration_time = current_dt - deacceleration_time;

            return start_pos + acceleration_distance + cruise_distance + new_max_velocity * deacceleration_time - 0.5 * temp_max_accel * deacceleration_time * deacceleration_time;
        }
    }

    public double motion_profile_vel(double current_dt) {

        if (current_dt > entire_dt)
            return 0;

        if (current_dt < acceleration_dt)
            return temp_max_accel*current_dt;

        else if (current_dt < deacceleration_time) {
            return new_max_velocity;
        }

        else {
            deacceleration_time = current_dt - deacceleration_time;

            return new_max_velocity-deacceleration_time;
        }
    }

    public double motion_profile_accel(double current_dt) {

        if (current_dt > entire_dt)
            return 0;

        if (current_dt < acceleration_dt)
            return temp_max_accel;

        else if (current_dt < deacceleration_time) {

            return 0;
        }

        else {
            return -temp_max_accel;
        }
    }

    public double getEntire_dt(){
        return entire_dt;
    }

    public boolean isOver(){
        return isOver;
    }

    public boolean isDone(){
        return isDone;
    }
}

