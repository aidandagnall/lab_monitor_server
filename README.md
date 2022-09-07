# Lab Monitor Server

See [Lab Monitor Client](https://github.aidandagnall.com/lab-monitor-client) for more information about this project.

If you'd like to work on this, you'll need your own MongoDB instance, and follow the schema in `schema.json`. To test
this with the client, you'll also need to set the `.env` variables in the client to point to your server.

Some routes are protected with an API key. You can set this yourself. It allows you to use Postman or similar to more
easily create/edit certain data, or just do it via MongoDB.
