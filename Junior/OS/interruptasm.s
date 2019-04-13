.section .text
    .global itable_start, itable_end
itable_start:
.word itable
itable:
    ldr pc, handler_reset_address
    ldr pc, handler_undefined_address
    ldr pc, handler_svc_address
    ldr pc, handler_prefetchabort_address
    ldr pc, handler_dataabort_address
    ldr pc, handler_reserved_address
    ldr pc, handler_irq_address
    ldr pc, handler_fiq_address
handler_reset_address: 
    .word handler_reset
handler_undefined_address:
    .word handler_undefined
handler_svc_address:
    .word handler_svc
handler_prefetchabort_address:
    .word handler_prefetchabort
handler_dataabort_address:
    .word handler_dataabort
handler_reserved_address:
    .word handler_reserved
handler_irq_address:
    .word handler_irq
handler_fiq_address:
    .word handler_fiq
itable_end:
.word .
handler_reset:
    
handler_undefined:
    ldr sp,=undfa_stack
	sub lr,lr,#4
	push {lr}
	push {r0-r12}
	
	bl handler_undefinded_c
	pop {r0-r12}
	pop {lr}
	subs pc,lr,#0
handler_svc:
	ldr sp,=svcfa_stack
	push {lr}
	push {r0-r12}
	mov r0,sp
	bl handler_svc_c
	pop {r0-r12}
	ldm sp!,{pc}^
	pop {lr}
	subs pc,lr,#0
handler_prefetchabort:
	ldr sp,=pfa_stack
	sub lr,lr,#4
	push {lr}
	push {r0-r12}
	
	bl handler_prefetchabort_c
	pop {r0-r12}
	pop {lr}
	subs pc,lr,#0

    
handler_dataabort:
	ldr sp,=pfda_stack
	sub lr,lr,#4
	push {lr}
	push {r0-r12}
	
	bl handler_dataabort_c
	pop {r0-r12}
	pop {lr}
	subs pc,lr,#0
	
handler_reserved:
    ldr sp,=resvfa_stack;
	sub lr,lr,#4
	push {lr}
	push {r0-r12}
	
	bl handler_reserved_c
	pop {r0-r12}
	pop {lr}
	subs pc,lr,#0
handler_irq:
    ldr sp,=irgfa_stack;
	sub lr,lr,#4
	push {lr}
	push {r0-r12}
	
	bl handler_irq_c
	pop {r0-r12}
	pop {lr}
	subs pc,lr,#0
handler_fiq:
    ldr sp,=fiqfa_stack;
	sub lr,lr,#4
	push {lr}
	push {r0-r12}
	
	bl handler_fig_c
	pop {r0-r12}
	pop {lr}
	subs pc,lr,#0
forever:
    b forever
	
.section .data
	.rept 1024
	.word 0
	.endr
pfa_stack:


	.rept 1024
	.word 0
	.endr
undfa_stack:


	.rept 1024
	.word 0
	.endr
svcfa_stack:


	.rept 1024
	.word 0
	.endr
irgfa_stack:


	.rept 1024
	.word 0
	.endr
fiqfa_stack:

	.rept 1024
	.word 0
	.endr
resvfa_stack:

	.rept 1024
	.word 0
	.endr
pfda_stack:
