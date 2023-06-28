package com.amirami.simapp.priceindicatortunisia.cartefidelite.firestore.usecases

data class UseCasesFidCard (
    val getFidCard: GetFidCard,
    val createUserFidCardDocument  : CreateUserFidCardDocument,
    val addListFidCard: AddListFidCard,
    val deleteFidCardUserDocument: DeleteFidCardUserDocument
)