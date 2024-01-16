<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class MasterController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        //
        return view('master');
    }
    public function checkServiceStatus(int $status){
        $stringStatus = '';
        switch ($status) {
            case -1:
                $stringStatus = 'Declined';
                break;
            case 0:
                $stringStatus = 'Pending';
                break;
            case 1:
                $stringStatus = 'In Progress';
                break;
            case 2:
                $stringStatus = 'In Review';
                break;
            case 3:
                $stringStatus = 'Success';
                break;
            case 4:
                $stringStatus = 'Fail';
                break;
            default:
                $stringStatus = 'Unknown';
        }
        return $stringStatus;
    }
    public function checkMyServiceStatus(int $status){
        $stringStatus = '';
        switch ($status) {
            case -1:
                $stringStatus = 'Expired/Declined';
                break;
            case 0:
                $stringStatus = 'Pending';
                break;
            case 1:
                $stringStatus = 'Active';
                break;
            case 2:
                $stringStatus = 'Cancel';
                break;
            default:
                $stringStatus = 'Unknown';
        }
        return $stringStatus;
    }
    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        //
    }
}
