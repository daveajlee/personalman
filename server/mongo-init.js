db.createUser(
        {
            user: "personalman",
            pwd: "myPMpassword",
            roles: [
                {
                    role: "readWrite",
                    db: "personalman"
                }
            ]
        }
);