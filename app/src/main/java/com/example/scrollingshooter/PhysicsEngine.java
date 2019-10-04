package com.example.scrollingshooter;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

class PhysicsEngine {

    // This signature and much more will
    //change later in the project
    boolean update(long fps, ArrayList<GameObject> objects,
                   GameState gs, SoundEngine se, ParticleSystem ps){

        // Update all the GameObjects
        for(GameObject o : objects){
            if(o.checkActive()){
                o.update(fps, objects.get(Level.PLAYER_INDEX).getTransform());
            }
        }
        if(ps.mIsRunning){
            ps.update(fps);
        }

        return detectCollisions(gs, objects, se, ps);
    }

    // Collision detection method will go here
    private boolean detectCollisions(GameState gs, ArrayList<GameObject> objects,
                                     SoundEngine se, ParticleSystem ps){

        boolean playerHit = false;
        for(GameObject go1 : objects){
            if(go1.checkActive()){
                // The ist object is active
                // so worth checking
                for(GameObject go2 : objects){
                    if(go2.checkActive()){
                        // The 2nd object is active
                        // so worth checking
                        if(RectF.intersects(go1.getTransform().getCollider(),
                                go2.getTransform().getCollider())){
                            // Switch goes here
                            // There has been a collision
                            // - but does it matter
                            switch(go1.getTag() + " with " +go2.getTag()){
                                case "Player with Alien Laser":
                                    playerHit = true;
                                    gs.loseLife(se);
                                    break;
                                case "Player with Alien":
                                    playerHit = true;
                                    gs.loseLife(se);
                                    break;
                                case "Player Laser with Alien":
                                    gs.increaseScore();
                                    // Respawn the alien
                                    ps.emitParticles(new PointF(go2.getTransform().getLocation().x,
                                            go2.getTransform().getLocation().y));
                                    go2.setInactive();
                                    go2.spawn(objects.get(Level.PLAYER_INDEX).getTransform());
                                    go1.setInactive();
                                    se.playAlienExplode();
                                    break;
                                default:
                                        break;
                            }
                        }
                    }
                }
            }
        }
        return playerHit;
    }

}
