package com.example.scrollingshooter;

import android.graphics.PointF;

import java.util.Random;

class AlienPatrolMovementComponent implements MovementComponent {

    private AlienLaserSpawner alienLaserSpawner;
    private Random mShotRandom = new Random();

    AlienPatrolMovementComponent(AlienLaserSpawner als){
        alienLaserSpawner = als;
    }

    @Override
    public boolean move(long fps, Transform t, Transform playerTransform){

        final int TAKE_SHOT = 1;
        final int SHOT_CHANCE = 100;

        // Where is the player
        PointF playerLocation = playerTransform.getLocation();

        // The top of the screen
        final float MIN_VERTICAL_BOUNDS = 0;
        // The width and height of the screen
        float screenX = t.getScreenSize().x;
        float screenY = t.getScreenSize().y;

        // How far ahead can the alien see?
        float mSeeingDistance = screenX * .5f;

        // Where is the alien?
        PointF location = t.getLocation();
        // How fast is the alien?
        float speed = t.getSpeed();
        // How tall is the alien
        float height = t.getObjectHeight();

        // Stop the alien going too far away
        final float MAX_VERTICAL_BOUNDS = screenY - height;
        final float MAX_HORIZONTAL_BOUNDS = screenX * 2;
        final float MIN_HORIZONTAL_BOUNDS = -screenX * 2;

        // Adjust the horizontal speed relative
        // to the player's heading
        // Default is no horizontal speed adjustment
        float horizontalSpeedAdjustmentRalativeToPlayer = 0;
        // How much to speed up or slow down relative
        // to player's heading
        float horizontalSpeedAdjustmentModifier = .8f;

        // Can the Alien "see" the player? If so make speed relative
        if(Math.abs(location.x - playerLocation.x) < mSeeingDistance){
            if(playerTransform.getFacingRight() != t.getFacingRight()){
                // Facing a different way speed up the alien
                horizontalSpeedAdjustmentRalativeToPlayer =
                        speed * horizontalSpeedAdjustmentModifier;
            } else {
                // Facing the same way slow it down
                horizontalSpeedAdjustmentRalativeToPlayer =
                        -(speed * horizontalSpeedAdjustmentModifier);
            }
        }
        // Move horizontally taking into account
        // the speed modification
        if(t.headingLeft()){
            location.x -= (speed + horizontalSpeedAdjustmentRalativeToPlayer) / fps;

            // Turn the ship around when it reaches the
            // extent of its horizontal patrol area
            if(location.x < MIN_HORIZONTAL_BOUNDS){
                location.x = MIN_HORIZONTAL_BOUNDS;
                t.headRight();
            }
        } else {
            location.x += (speed + horizontalSpeedAdjustmentRalativeToPlayer) / fps;

            // Turn the ship around when it reaches the
            // extent of its horizontal patrol area
            if(location.x > MAX_HORIZONTAL_BOUNDS){
                location.x = MAX_HORIZONTAL_BOUNDS;
                t.headLeft();
            }
        }

        // Vertical speed remains same,
        // Not affected by speed adjustment
        if(t.headingDown()){
            location.y += (speed) / fps;
            if(location.y > MAX_VERTICAL_BOUNDS){
                t.headUp();
            }
        } else {
            location.y -= (speed) / fps;
            if(location.y < MIN_VERTICAL_BOUNDS){
                t.headDown();
            }
        }

        // Update the collider
        t.updateCollider();

        // Shoot if the alien within a ships height above,
        // below, or in line with the player?
        // This could be a hit or a miss
        if(mShotRandom.nextInt(SHOT_CHANCE) == TAKE_SHOT){
            if(Math.abs(playerLocation.y - location.y) < height){
                // is the alien facing the right direction
                // and close enough to the player
                if((t.getFacingRight() && playerLocation.x > location.x
                    || !t.getFacingRight() && playerLocation.x < location.x)
                && Math.abs(playerLocation.x - location.x) < screenX){
                    // Fire!
                    alienLaserSpawner.spawnAlienLaser(t);
                }
            }
        }
        return true;
    }
}
