using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;


public class playerMovement : MonoBehaviour {
    const int playerSpeed = 4;
    int daysRemaining = 20;

    public bool moveUpAllowed = true;
    public bool moveDownAllowed = true;
    public bool moveRightAllowed = true;
    public bool moveLeftAllowed = true;
    bool isLocked = false;
    bool saveUp = true;
    bool saveDown = true;
    bool saveRight = true;
    bool saveLeft = true;

    Vector3 motion = new Vector3();
    // Use this for initialization
    void Start () {
        if (GameObject.Find("dataPassingObject") != null)
        {
            daysRemaining = GameObject.Find("dataPassingObject").GetComponent<dataPasser>().getDays();
            GameObject.Find("player").GetComponent<journal>().setJournal(GameObject.Find("dataPassingObject").GetComponent<dataPasser>().getJournal());
        }
    }

    public void lockPlayer()
    {
        if (isLocked == false)
        {
            saveUp = moveUpAllowed;
            moveUpAllowed = false;

            saveDown = moveDownAllowed;
            moveDownAllowed = false;

            saveRight = moveRightAllowed;
            moveRightAllowed = false;

            saveLeft = moveLeftAllowed;
            moveLeftAllowed = false;
            isLocked = true;
        }
        

    }

    public void panic()
    {
        moveUpAllowed = true;
        moveDownAllowed = true;
        moveLeftAllowed = true;
        moveRightAllowed = true;
    }

    public void unlockPlayer()
    {
        if (isLocked == true)
        {
            moveUpAllowed = saveUp;
            moveDownAllowed = saveDown;
            moveRightAllowed = saveRight;
            moveLeftAllowed = saveLeft;
            isLocked = false;
        }



    }
    public int getDays()
    {
        return daysRemaining;
    }

    public void setDays(int newDays)
    {
        daysRemaining = newDays;
    }

    Vector3 cartesian_to_isometric(Vector3 cart)
    {
		Vector3 iso = new Vector3(cart.x - cart.y, (cart.x + cart.y) / 2, cart.z);
        return iso;

    }

    Vector3 isometric_to_cartesian(Vector3 iso)
    {
        Vector3 cart = new Vector3();
        cart.x = (iso.x + iso.y * 2) / 2;
        cart.y = -iso.x + cart.x;
		cart.z = iso.z;

        return cart;
    }
	// Update is called once per frame
	void Update () {
        //Check Game state
        if(daysRemaining <= 0)
        {
            SceneManager.LoadScene("gameOver");
        }
        //Handle the movement
        Vector3 direction = new Vector3();

        if(Input.GetKey("w") & moveUpAllowed)
        {
            direction += new Vector3(0.0f, 1.0f, 0.4f);
        }
        else if (Input.GetKey("s") & moveDownAllowed)
        {
            direction += new Vector3(0.0f, -1.0f, -0.4f);
        }
        else if (Input.GetKey("a") & moveLeftAllowed)
        {
            direction += new Vector3(-1.0f, 0.0f, -0.4f);
        }
        else if (Input.GetKey("d") & moveRightAllowed)
        {
            direction += new Vector3(1.0f, 0.0f, 0.4f);
        }

        motion = direction.normalized * playerSpeed * Time.deltaTime;
        motion = cartesian_to_isometric(motion);
		Vector3 toPos = new Vector3(motion.x, motion.y, motion.z);
        transform.position = transform.position + toPos;
        
        //Handle z capping
        Scene scene = SceneManager.GetActiveScene();
        Vector3 newPos = transform.position;
        
        switch (scene.name)
        { 
            case ("forest"):
                if (newPos.z <= -14.0f)
                {
                    Vector3 corZPos = new Vector3(newPos.x, newPos.y, -14.0f);
                    transform.position = corZPos;
                }
                else if (newPos.z >= 20.0f)
                {
                    Vector3 corZPos = new Vector3(newPos.x, newPos.y, 20.0f);
                    transform.position = corZPos;
                }
                break;
            case ("town"):
                if (newPos.z <= 6.0f)
                {
                    Vector3 corZPos = new Vector3(newPos.x, newPos.y, 6.0f);
                    transform.position = corZPos;
                }
                else if (newPos.z >= 56.0f)
                {
                    Vector3 corZPos = new Vector3(newPos.x, newPos.y, 56.0f);
                    transform.position = corZPos;
                }
                break;
        }

        //Handle updating the UI
        Text mTextBox = GameObject.Find("brotherHealthTextBox").GetComponent<Text>();
        mTextBox.text = "Days Remaining: " + daysRemaining.ToString();

        //escape game
        if(Input.GetKey("p"))
        {
            savingLoading mScript = GameObject.Find("player").GetComponent<savingLoading>();
            mScript.Save();
            Application.Quit();
        }

    }
    public void addDays(int daysToAdd)
    {
        daysRemaining += daysToAdd;
    }
    public void decreaseDay()
    {
        daysRemaining--;
    }
    public void moveSet(string direct, bool setting)
    {
        if(direct == "up")
        {
            moveUpAllowed = setting;
        }
        if (direct == "down")
        {
            moveDownAllowed = setting;
        }
        if (direct == "left")
        {
            moveLeftAllowed = setting;
        }
        if (direct == "right")
        {
            moveRightAllowed = setting;
        }
    }
}
