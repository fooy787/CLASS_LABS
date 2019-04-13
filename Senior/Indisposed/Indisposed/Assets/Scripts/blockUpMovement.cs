using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class blockUpMovement : MonoBehaviour
{
    playerMovement sn = null;
    // Use this for initialization
    void OnTriggerEnter2D(Collider2D upHit)
    {
        GameObject tmp = GameObject.FindWithTag("Player");
        GameObject soundFile = GameObject.Find("bumpWood");
        soundFile.GetComponent<AudioSource>().Play();
        sn = tmp.GetComponent<playerMovement>();
        if(this.tag == "up")
            sn.moveSet("up", false);
        if (this.tag == "left")
            sn.moveSet("left", false);
        if (this.tag == "right")
            sn.moveSet("right", false);
        if (this.tag == "down")
            sn.moveSet("down", false);
    }
    void OnTriggerExit2D(Collider2D upHit)
    {
        Debug.Log("gets in colider");
        GameObject tmp = GameObject.FindWithTag("Player");
        sn = tmp.GetComponent<playerMovement>();
        if (this.tag == "up")
            sn.moveSet("up", true);
        if (this.tag == "left")
            sn.moveSet("left", true);
        if (this.tag == "right")
            sn.moveSet("right", true);
        if (this.tag == "down")
            sn.moveSet("down", true);
    }
}
