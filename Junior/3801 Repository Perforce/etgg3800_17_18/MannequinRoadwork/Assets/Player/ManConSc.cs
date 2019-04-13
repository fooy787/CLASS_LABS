using System.Collections;
using System.Collections.Generic;
using UnityEngine;


[RequireComponent(typeof (Animator))]
[RequireComponent(typeof (CapsuleCollider))]
[RequireComponent(typeof (Rigidbody))]


public class ManConSc : MonoBehaviour {

    [System.NonSerialized]
    public float lookWeight;

    [System.NonSerialized]
    public Transform enemy;

    public float animSpeed = 1.5f;
    public float lookSmoother = 3f;
    public bool useCurves;
    
    private Animator anim;
    private AnimatorStateInfo CurrentBaseState;
    private AnimatorStateInfo layer2CurrentSate;
    private CapsuleCollider col;

    static int idleState = Animator.StringToHash("Base Layer.Idle");
    static int locoState = Animator.StringToHash("Base Layer.Locomotion");

    



    // Use this for initialization
    void Start () {

        anim = GetComponent<Animator>();
        col = GetComponent<CapsuleCollider>();
        enemy = GameObject.Find("Enemy").transform;
        anim.SetLayerWeight(1, 1);
		
	}
	
	// Update is called once per frame
	void Update () {

        float h = Input.GetAxis("Horizontal");
        float v = Input.GetAxis("Vertical");
        anim.SetFloat("Speed", v);
        anim.SetFloat("Direction", h);
        anim.speed = animSpeed;
        anim.SetLookAtWeight(lookWeight);

		
	}
}
