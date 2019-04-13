using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Control : MonoBehaviour
{

    [SerializeField]
    private Animator animator;
    [SerializeField]
    private float DirectionDampTime = .25f;

    public float Speed = -0.9f;
    public float Direction = 0.0f;

    public float turnSpeed = 0.0f;

    public float _Velocity = 0.0f;      // Current Travelling Velocity
    public float _MaxVelocity = 0.0f;   // Max Velocity
    public float _Acc = 0.0f;           // Current Acceleration
    public float _AccSpeed = 0.0f;      // Amount to increase Acceleration with.
    public float _MaxAcc = 0.0f;        // Max Acceleration
    public float _MinAcc = -1.0f;       // Min Acceleration

    // Use this for initialization
    void Start()
    {

        animator = GetComponent<Animator>();

        if (animator.layerCount >= 2)
        {
            animator.SetLayerWeight(1, 1);
        }
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKey("w"))
        {
            _Acc += _AccSpeed;
            Speed += 0.03f;
            if (Speed >= 4 )
            {
                Speed = 4;
            }
        }
        else
        {
            Speed -= 0.06f;
            _Acc -= _AccSpeed;

            if (Speed <= -0.01f)
            {
                Speed = 0.0f;
            }
        }

        if (Input.GetKey("s"))
        {
            
            _Acc -= _AccSpeed;
          
        }

        if (Input.GetKey("a"))
        {
            transform.Rotate(Vector3.up, -turnSpeed * Time.deltaTime);
        }

        if (Input.GetKey("d"))
        {
            transform.Rotate(Vector3.up, turnSpeed * Time.deltaTime);
        }

        if (_Acc > _MaxAcc)
        {
            _Acc = _MaxAcc;
        }

        else if (_Acc < _MinAcc)
    
            _Acc = _MinAcc;
        _Velocity += _Acc;
        if (_Acc <= 0)
            _Velocity = 0;

        if (_Velocity > _MaxVelocity)
            _Velocity = _MaxVelocity;
        else if (_Velocity < -_MaxVelocity)
        {
            _Velocity = -_MaxVelocity;
     
        }

        transform.Translate(Vector3.forward * _Velocity * Time.deltaTime);

        

        if (animator)
        {
            animator.SetFloat("Speed", Speed);
            animator.SetFloat("Direction", Input.GetAxis("Horizontal"), DirectionDampTime, Time.deltaTime);
        }

    }

    private void OnDrawGizmos()
    {

    }
}
