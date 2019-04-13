using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class interactionScript : MonoBehaviour
{
    static textBox textB;
    //static int initVar = 0;  //this is so it only finds the objects once (saves time)
    static List<string> textToScreen = new List<string>();
    static bool display = false;
    static List<string> currentObj = new List<string>();
    static List<string> yesOp = new List<string>(); // If 'R' then you exit the script, 'L' first is for level change and [1] will be going to, [2] would be coming from [3] would be any text that would display.
    static List<string> noOp = new List<string>(); // If 'T' then load text to the textToScreen, and change prompt to -1 and then set curTextCount to 0 again.
    static int curTextCount = 0;
    static int hasPrompt = -1;
    static string letterCheck;
    static GameObject playerObj;
    static playerMovement playerScript;
    static levelChanger sn = null;
    static bool changedLevel = false;

    /// <summary>
    /// This Inits all the variables and gets all the needed scripts that will be used. Such as, player and level changer script.
    /// </summary>
    void Start()
    {
            GameObject.Find("thoughtBubble").GetComponent<SpriteRenderer>().enabled = false;
            textB = (textBox)GameObject.Find("textBoxMaker").GetComponent(typeof(textBox));
            playerObj = GameObject.FindWithTag("Player");
            playerScript = playerObj.GetComponent<playerMovement>();
            GameObject tmp = GameObject.FindWithTag("blackScreen");
            sn = (levelChanger)tmp.GetComponent(typeof(levelChanger)); 
    }

    /// <summary>
    /// This is the update loop that tracks key presses and if anything needs to be registered for the text boxes.
    /// </summary>
    void Update()
    {
        // If the level is changed, this will reset the text box if there was anything displayed before the level change.
        // This also is where it decreasses the days if sleeping was the level change.
        if (sn.canPlayerMove() && changedLevel == true)
        {
            if (currentObj[currentObj.Count - 1] == "playerBed")
            {
                Debug.Log("calls decreaseDays");
                playerScript.decreaseDay();
            }
            display = false;
            currentObj.Clear();
            changedLevel = false;
            textB.switchOff();
        }

        // This is where a text box will be first open and set up for display.
        if (Input.GetKeyDown("space") && display == false && currentObj.Count != 0)
        {
            textB.switchOn();
            this.checkTag(currentObj[currentObj.Count - 1]);
            textB.changeText(textToScreen[curTextCount]);
            playerScript.lockPlayer();
           
            display = true;
        }
        // This is where if the prompt is being displayed what to do with it.
        else if (hasPrompt == curTextCount && display == true)
        {
            if (Input.GetKeyDown("e"))
            {
                letterCheck = yesOp[0];
                if(letterCheck == "R")
                {
                    playerScript.unlockPlayer();
                    display = false;
                    textB.switchOff();
                    this.checkTag(currentObj[currentObj.Count - 1]);
                }
                else if(letterCheck == "L")
                {
                    if(yesOp.Count == 4)
                    {
                        textB.changeText(yesOp[3]);
                    }
                    sn.FadeToLevel(yesOp[1], yesOp[2]);
                    changedLevel = true;
                }
                // **To add**
                // if letter cheeck is T then load all the the text into the textToScreen resetting the count to 0 too
            }
            else if(Input.GetKeyDown("q"))
            {
                letterCheck = noOp[0];
                if (letterCheck == "R")
                {
                    playerScript.unlockPlayer();
                    display = false;
                    textB.switchOff();
                    this.checkTag(currentObj[currentObj.Count - 1]);
                }
                else if (letterCheck == "L")
                {
                    if (yesOp.Count == 4)
                    {
                        textB.changeText(yesOp[3]);
                    }
                    sn.FadeToLevel(yesOp[1], yesOp[2]);
                    changedLevel = true;
                }
            }
        }
        // This is what to do if space is hit and there is no prompt.
        else if (Input.GetKeyDown("space") && display == true)
        {
            if(curTextCount < textToScreen.Count - 1)
            {
                curTextCount++;
                textB.changeText(textToScreen[curTextCount]);
            }
            else
            {
                display = false;
                textB.switchOff();
                playerScript.unlockPlayer();
            }
            
        }
       
        // This is the panic button that will release the players movement if they are stuck.
        if (Input.GetKeyDown("p"))
        {
            playerScript.panic();
        }

    }

    /// <summary>
    /// When you spawn on a hit box, this will make sure the object is properly added to the list to interact with.
    /// </summary>
    /// <param name="box"> This is just required to be here incase there are uses for changing the hit box. </param>
    void OnTriggerStay2D(Collider2D box)
    {
        GameObject.Find("thoughtBubble").GetComponent<SpriteRenderer>().enabled = true;
        if (!currentObj.Contains(this.tag))
        {
            currentObj.Add(this.tag);
        }
    }

    /// <summary>
    /// This will look in the list and remove the object that you walk away from, from the list.
    /// </summary>
    /// <param name="upHit"> This is just required to be here incase there are uses for changing the hit box. </param>
    void OnTriggerEnter2D(Collider2D upHit)
    {
        GameObject.Find("thoughtBubble").GetComponent<SpriteRenderer>().enabled = true;
        if (!currentObj.Contains(this.tag))
        {
            currentObj.Add(this.tag);
        }
    }

    /// <summary>
    /// When you walk by an object this will queue it up as potentally being displayed.
    /// </summary>
    /// <param name="upHit"> This is just required to be here incase there are uses for changing the hit box. </param>
    void OnTriggerExit2D(Collider2D upHit)
    {
        GameObject.Find("thoughtBubble").GetComponent<SpriteRenderer>().enabled = false;
        if (currentObj.Contains(this.tag))
        {
            currentObj.Remove(this.tag);
        }
    }

    /// <summary>
    /// This is the set up for each of the tags in the world for what it should say.
    /// </summary>
    /// <param name="passed"> This is the object you want to set up for interacting with. </param>
    private void checkTag(string passed)
    {
        switch (passed)
        {
            case "playerBed":
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nAre you sure you want to sleep tonight?\n\n\n(E) Yes\t\t\t(Q) No");
                hasPrompt = 0;
                yesOp.Add("L");
                yesOp.Add("sleep");
                yesOp.Add("insidePlayerHouse");
                yesOp.Add("zzz");
                noOp.Add("R");
                curTextCount = 0;
                break;

            case "fireplace":
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nNice place to warm up and relax by....");
                textToScreen.Add("\n Well back to work!");
                hasPrompt = -1;
                curTextCount = 0;
                break;

            case "chest":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nyou have ____$ stored.");
                break;

            case "bucket":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nJust an old bucket you use to bring water inside.");
                break;

            case "ax":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nI don't think we need anymore firewood for the fire right now.");
                break;

            case "potion":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nStrange mixes of herbs that your parents made before. You cant recall what they do.");
                break;

            case "broom":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nSweep sweep!");
                break;

            case "dresser":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nThis is no time to fix your hat!");
                break;

            case "nightstand":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nThis is where your brother keeps his random rocks he collects outside.");
                break;

            case "butterChurn":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nThis is no time for butter making!");
                break;

            case "crock":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nThis is where you store water for everyday tasks.");
                break;

            case "brotherBed":
                hasPrompt = -1;
                curTextCount = 0;
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                textToScreen.Add("\nhmmmm where is my brother now");
                break;

            default:
                textToScreen.Clear();
                yesOp.Clear();
                noOp.Clear();
                hasPrompt = -1;
                curTextCount = 0;
                break;
        }
    }
}