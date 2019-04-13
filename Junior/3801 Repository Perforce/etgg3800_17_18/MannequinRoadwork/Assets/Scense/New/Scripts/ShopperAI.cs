using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;


public class ShopperAI : MonoBehaviour {
    public float avgWaitTime;
    public float waitVarince;
    public float minDistaneToPoint;
    public float fov;

    public Transform[] points;
    public int currentPoint;
    public float waitTime;
    public bool isWaiting;

    public float maxTimeAlerted;
    private NavMeshAgent agent;
    private float timeAlerted;

    public void init(Transform[] points)
    {
        agent = GetComponent<NavMeshAgent>();
        isWaiting = true;
        currentPoint = -1;
        this.points = points;
    }

   public void Update()
    {
        updateAI();
        if (checkForPlayerSeen())
        {
            timeAlerted += Time.deltaTime;
            if (timeAlerted >= maxTimeAlerted) {
                print("You Lose");
                Time.timeScale = 0;
            }
        }
        else {
            timeAlerted = 0;
        }
    }

    private bool checkForPlayerSeen() {
        GameObject Player = GameObject.FindGameObjectWithTag("Player");
        if (!Player) {
            return false;
        }
        RaycastHit hit;
        Debug.DrawRay(transform.position, transform.forward * 100, Color.magenta);
        if (Physics.Raycast(transform.position, (Player.transform.position-transform.position).normalized, out hit))
        {
            Debug.DrawRay(transform.position, (Player.transform.position - transform.position), Color.cyan);
            if (hit.collider.gameObject == Player)
            {
                Vector3 playerDir = Player.transform.position - transform.position;
                float angle = Vector3.Angle(playerDir, transform.forward);
                return angle < fov;
            }
        }
        else {
            print("?");
        }
        return false;
    }

    private void updateAI() {

        if (isWaiting)
        {
            waitTime -= Time.deltaTime;
            if (waitTime <= 0)
            {
                currentPoint += 1;
                isWaiting = false;
                if (currentPoint < points.Length)
                {
                    agent.SetDestination(points[currentPoint].position);
                }
                else
                {
                    //leave store
                    GameObject.Destroy(gameObject);
                }
            }
        }
        else
        {
            if (currentPoint < points.Length)
            {
                if ((points[currentPoint].position - transform.position).magnitude < minDistaneToPoint)
                {
                    isWaiting = true;
                    waitTime = Mathf.Max(0, avgWaitTime + Random.Range(-waitVarince, waitVarince));
                }
            }
        }
    }

}
