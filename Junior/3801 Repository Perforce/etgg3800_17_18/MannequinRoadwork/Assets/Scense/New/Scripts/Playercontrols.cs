using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Playercontrols : MonoBehaviour {
    public float speed = 10;
    public float rotSpeed = 5;

    public Transform exit;
    public float winDistance;

    public GameObject pauseMenu;
	void Update () {
        float translation = Input.GetAxis("Vertical") * speed * Time.deltaTime;
        float rotation = Input.GetAxis("Horizontal") * rotSpeed * Time.deltaTime;
        transform.Translate(0, 0, translation);
        transform.Rotate(0, rotation, 0);
        if ((exit.position - transform.position).magnitude <= winDistance) {
            print("You Win");
            Time.timeScale = 0;
        }
    }
}
